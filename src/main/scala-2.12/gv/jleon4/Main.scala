package gv
package jleon4

import java.net.{ URI ⇒ JUri }

import java.nio.file.{ Path ⇒ JPath, FileSystem ⇒ JFileSystem, FileAlreadyExistsException, NoSuchFileException }
import java.nio.channels.{ ReadableByteChannel, WritableByteChannel, Channels }

import language.{ postfixOps, implicitConversions, higherKinds, existentials }
import util.{ Try, Success, Failure }
import concurrent.{ Future, ExecutionContext, Await }
import concurrent.duration._

import akka.stream.scaladsl.{ Source, Flow, Sink }
import akka.stream.{ Materializer, ActorMaterializer }
import akka.actor.{ ActorSystem }
import akka.http.scaladsl.server.{ Route, Directives, Directive0 }
import akka.http.scaladsl.{ Http }

import com.typesafe.config.{ Config ⇒ TSConfig, ConfigFactory ⇒ TSConfigFactory }
import com.typesafe.scalalogging.{ StrictLogging, Logger }

import shapeless.{ HNil, ::, HList }

import isi.convertible._
import isi.std.conversions._
import isi.{ ~> }

import gv.{ jleon4 ⇒ app }

trait TypeClassPackage {

  trait TypeClass[-Self] extends Any {
    type Out

    def apply(self: Self): Out
  }

  object TypeClass {
    trait WithTypeParams[-Self, out] extends Any with TypeClass[Self] {
      final type Out = out
    }
  }

  trait TypeClassCompanion[TC[T] <: TypeClass[T]] {
    def apply[T: TC]: TC[T] = implicitly
    def apply[T](self: T)(implicit tc: TC[T]): tc.Out = tc(self)
  }

}

trait ConfigPackage {
  this: TypeClassPackage ⇒

  trait Config[-T] extends Any with TypeClass.WithTypeParams[T, Config.Ops]

  object Config extends TypeClassCompanion[Config] {
    trait Ops extends Any {
      type Config <: TSConfig
      type FileSystem <: JFileSystem
      def config: Config
      def fileSystem: FileSystem

      final def getPath: String ⇒ JPath = (config getString _) andThen (fileSystem getPath _)
      final def getUri: String ⇒ JUri = (config getString _) andThen (java.net.URI create)
    }

    object Ops {
      implicit def toTSConfig(ops: Ops): TSConfig = ops.config
    }
  }

  implicit def configOps[T: Config](self: T): Config.Ops = Config[T](self)
}

trait ConfigInterpretationsPackage extends Any {
  // format: OFF
  this: Any
    with Util
    with ConfigPackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordConfig[
    config: CouldBe[TSConfig]#t,
    fileSystem: CouldBe[JFileSystem]#t,
    rec <: HList
  ]: // format: ON
  Config[config :: fileSystem :: rec] = self ⇒ new Config.Ops {
    final type Config = TSConfig
    final type FileSystem = JFileSystem
    final val (config: Config) :: (fileSystem: FileSystem) :: _ = self
  }

}

trait PathPackage {
  this: TypeClassPackage ⇒

  trait Path[T] extends Any with TypeClass.WithTypeParams[T, Path.Ops]

  object Path extends TypeClassCompanion[Path] {

    trait Ops extends Any {
      type Self <: JPath
      def self: Self

      final def addExt(ext: String): JPath = self resolveSibling s"${self.getFileName}.$ext"
    }

    final object Ops
  }

}

object PathInterpretationsPackage extends PathPackage with TypeClassPackage {
  implicit class JPathPath(val self: JPath) extends AnyVal with Path.Ops {
    final type Self = JPath
  }
}

trait StorageMapPackage {
  this: TypeClassPackage ⇒

  import PathInterpretationsPackage._

  trait StorageMap[-T] extends TypeClass.WithTypeParams[T, StorageMap.Ops]

  object StorageMap extends TypeClassCompanion[StorageMap] {

    final val EXT_LOCK = "lock"
    final val EXT_FAILURE = "failed"

    trait Ops extends Any {
      type Base <: JPath
      def base: Base

      final class ForItem(val item: String) {
        val storage: JPath = base resolve item
        val lock: JPath = storage addExt EXT_LOCK
        val failure: JPath = storage addExt EXT_FAILURE
      }

      final def apply(item: String): ForItem = new ForItem(item)
    }

    object Ops
  }

  implicit def storageMapOps[T: StorageMap](self: T): StorageMap.Ops = StorageMap[T](self)
}

trait StorageMapInterpretationsPackage extends Any {
  // format: OFF
  this: Any
    with Util
    with StorageMapPackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordStorageMap[
    base: CouldBe[JPath]#t,
    rec <: HList
  ]: // format: ON
  StorageMap[base :: rec] = self ⇒ new StorageMap.Ops {
    final type Base = JPath
    final val baseSource :: _ = self
    final val base: Base = baseSource
  }
}

trait StoragePackage {
  // format: OFF
  this: Any
    with Util
    with TypeClassPackage
    with StorageMapPackage
  ⇒
  // format: ON

  sealed trait LockResult
  final object LockResult {
    case class Acquired(channel: WritableByteChannel) extends LockResult
    case class Found(channel: ReadableByteChannel) extends LockResult

    case class Locked(item: String, cause: FileAlreadyExistsException)
      extends Exception(s"item is locked: $item", cause) with LockResult

    case class Failed(item: String)
      extends Exception(s"item is failed: $item") with LockResult
  }

  trait Storage[-T] extends TypeClass.WithTypeParams[T, Storage.Ops]

  object Storage extends TypeClassCompanion[Storage] {

    val nothing: Try[LockResult] = Failure(new Exception("nothing happened yet"))

    trait Ops {
      import java.nio.file.{ Files, StandardOpenOption ⇒ oopt }

      type StorageMap <: StoragePackage.this.StorageMap.Ops
      def storageMap: StorageMap

      final class ForItem(item: String) {
        val map = storageMap(item)

        val getFailure: Try[LockResult] =
          if (Files exists map.failure)
            Success(LockResult.Failed(map.item))
          else
            Failure(new NoSuchFileException(map.failure.toString))

        val getLock: Try[LockResult] = Try {
          Files newByteChannel (map.lock, oopt.CREATE_NEW, oopt.WRITE) close ()
        } map { _ ⇒
          Files newByteChannel (map.storage, oopt.CREATE_NEW, oopt.WRITE)
        } map {
          LockResult.Acquired.apply
        } recover {
          case ex: FileAlreadyExistsException ⇒ LockResult.Locked(map.item, ex)
        }

        val getStorage: Try[LockResult] = Try {
          Files newByteChannel (map.storage, oopt.READ)
        } map {
          LockResult.Found.apply
        }

        val tryLock: Try[LockResult] =
          nothing recoverWith pf(getFailure) recoverWith pf(getLock) recoverWith pf(getStorage)
      }

      final def apply(item: String): ForItem = new ForItem(item)

      final val tryLock: String ⇒ Try[LockResult] = apply _ andThen (_.tryLock)
    }
  }

  implicit def storageOps[T: Storage](self: T): Storage.Ops = Storage[T](self)

}

trait StorageInterpretationsPackage {
  // format: OFF
  this: Any
    with Util
    with StorageMapPackage
    with StoragePackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordStorage[
    storageMap: CouldBe[StorageMap.Ops]#t,
    rec <: HList
  ]: // format: ON
  Storage[storageMap :: rec] = self ⇒ new Storage.Ops {
    final type StorageMap = StorageMap.Ops
    final val storageMapSource :: _ = self
    final val storageMap: StorageMap = storageMapSource
  }

}

trait StorageFactoryPackage {
  // format: OFF
  this: Any
    with ConfigPackage
    with Util
    with TypeClassPackage
    with StorageMapPackage
    with StorageMapInterpretationsPackage
    with StoragePackage
    with StorageInterpretationsPackage
  ⇒
  // format: ON

  trait StorageFactory[-T] extends Any with TypeClass.WithTypeParams[T, StorageFactory.Ops]

  //noinspection TypeAnnotation
  object StorageFactory extends TypeClassCompanion[StorageFactory] {

    trait Ops extends Any {
      type Config <: StorageFactoryPackage.this.Config.Ops

      def config: Config

      final def storageMap = couldBe[StorageMap.Ops] {
        (config getUri "basePath") :: HNil
      }

      final def storage = couldBe[Storage.Ops] {
        storageMap :: HNil
      }
    }

    object Ops

  }

  implicit def storageFactoryOps[T: StorageFactory](self: T): StorageFactory.Ops = StorageFactory[T](self)
}

trait StorageFactoryInterpretationsPackage extends Any {
  // format: OFF
  this: Any
    with Util
    with ConfigPackage
    with StorageFactoryPackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordStorageFactory[
    config: CouldBe[Config.Ops]#t,
    rec <: HList
  ] // format: ON
  : StorageFactory[config :: rec] = self ⇒ new StorageFactory.Ops {
    final type Config = StorageFactoryInterpretationsPackage.this.Config.Ops
    final val configSource :: _ = self
    final val config: Config = configSource
  }

}

object Main extends AnyRef
    with StrictLogging
    with Util
    with TypeClassPackage
    with ConfigPackage
    with ConfigInterpretationsPackage
    with PathPackage
    with StorageMapPackage
    with StorageMapInterpretationsPackage
    with StorageFactoryPackage
    with StorageFactoryInterpretationsPackage
    with StoragePackage
    with StorageInterpretationsPackage {
  app ⇒
  import PathInterpretationsPackage._

  //noinspection TypeAnnotation
  final case class Factory(
      tsconfig:   TSConfig    = TSConfigFactory.defaultApplication,
      fileSystem: JFileSystem = java.nio.file.FileSystems.getDefault
  ) {
    private[this] val config = couldBe[Config.Ops] { (tsconfig getConfig "jleon") :: fileSystem :: HNil }

    private[this] val storageConfig = couldBe[Config.Ops] { (config.config getConfig "storage") :: fileSystem :: HNil }

    val storageFactory = couldBe[StorageFactory.Ops] { storageConfig :: HNil }
  }

  final case class Main() {
    val factory: Factory = Factory()
  }

  def randomTest(): Unit = {

    val path = java.nio.file.Paths.get("hibob")
    val smap = path :: HNil
    val ops = implicitly[StorageMap[smap.type]]
    println(ops(smap).base)
    println(ops(smap).base.addExt("lol"))

    val tsconfig: TSConfig = TSConfigFactory.defaultApplication
    val fs: JFileSystem = java.nio.file.FileSystems.getDefault
    val self2 = tsconfig :: fs :: HNil
    val ops2 = implicitly[Config[self2.type]]
    println(ops2(self2).getUri("jleon.storage.basePath"))

    println {
      val bobo = Main().factory.storageFactory.storageMap("bobo")
      import bobo._
      (lock, failure, storage)
    }

  }

  def main(args: Array[String]): Unit = {
    val main = Main()
    val sto = main.factory.storageFactory.storage

    println {
      (1 to 2) map (_ ⇒ sto.tryLock("mani"))
    }
  }

}
