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


object Main extends AnyRef
    with Util
    with StrictLogging
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
