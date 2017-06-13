package gv
package jleon4

//import java.net.{ URI ⇒ JUri }

import java.nio.{ ByteBuffer }
//import java.nio.file.{ Path ⇒ JPath, FileSystem ⇒ JFileSystem, FileAlreadyExistsException, NoSuchFileException }
//import java.nio.channels.{ ReadableByteChannel, WritableByteChannel, Channels }

import language.{
//  postfixOps
//  ,
  implicitConversions
//  ,
//  higherKinds
//  ,
//  existentials
}
//import util.{ Try, Success, Failure }
//import concurrent.{ Future, ExecutionContext, Await }
//import concurrent.duration._

//import akka.stream.scaladsl.{ Source, Flow, Sink }
//import akka.stream.{ Materializer, ActorMaterializer }
//import akka.actor.{ ActorSystem }
//import akka.http.scaladsl.server.{ Route, Directives, Directive0 }
//import akka.http.scaladsl.{ Http }

//import com.typesafe.config.{
//  Config ⇒ TSConfig,
//  ConfigFactory ⇒ TSConfigFactory
//}
//import com.typesafe.scalalogging.{ StrictLogging, Logger }

//import shapeless.{ HNil, ::, HList }

import isi.convertible._
//import isi.std.conversions._
//import isi.std.io._
//import isi.akka._
//import isi.{ ~~> }

//import gv.{ jleon4 ⇒ app }

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

  trait ResourcePackage {
    // format: OFF
    this: Any
      with TypeClassPackage
    ⇒
    // format: ON

    trait Resource[T] extends TypeClass.WithTypeParams[T, Resource.Ops]

    object Resource extends TypeClassCompanion[Resource] {
      trait Ops extends Any {
        def apply(uri: Uri): Source[ByteString, NotUsed]
      }

      final implicit def resourceOps[T: Resource](self: T): Resource.Ops = Resource[T](self)
    }
  }

  trait FallBackResourcePackage {
    // format: OFF
    this: Any
      with TypeClassPackage
      with ResourcePackage
    =>
    // format: ON

    trait FallBackResource[T] extends TypeClass.WithTypeParams[T, FallBackResource.Ops]

    object FallBackResource extends TypeClassCompanion[FallBackResource] {
      trait Ops extends Any with Resource.Ops {
        type FallBack
        def fallBack: FallBack
        implicit def fallBackOps: CouldBe[Resource.Ops]#t[FallBack]

        def applyImpl(uri: Uri): Source[ByteString, NotUsed]

        final def apply(uri: Uri): Source[ByteString, NotUsed] =
          applyImpl(uri) recoverWithRetries (1, pf(fallBack(uri)))
      }
    }

    final implicit def fallBackResourceOps[T: FallBackResource](self: T): FallBackResource.Ops = FallBackResource[T](self)
  }

  trait CachingResourcePackage {
    // format; OFF
    app: Any
      with TypeClassPackage
      with ResourcePackage
      with StoragePackage
    ⇒
    // format: ON
    trait CachingResource[T] extends TypeClass.WithTypeParams[T, CachingResource.Ops]

    object CachingResource extends TypeClassCompanion[CachingResource] {

      import isi.std.io._
      import isi.akka._
      import isi.concurrent.Executors.{ SyncExecutor ⇒ executeNow }

      final type Out = Source[ByteString, Future[Done]]

      private[this] val handleFound: LockResult ~~> Try[Out] = {
        case LockResult.Found(ins) ⇒ Success {
          ins
            .convertTo[Stream[ByteString]]
            .convertTo[Source[ByteString, NotUsed]]
            .mapMaterializedValue(_ ⇒ Future successful Done)
        }
      }

      private[this] def makeAcquiredSink(outs: WritableByteChannel): Sink[ByteString, Future[Done]] =
        outs
          .convertTo[Sink[ByteBuffer, Future[Int]]]
          .convertTo[Sink[ByteString, Future[Int]]]
        .mapMaterializedValue(_.map(_ ⇒ Done)(executeNow))

      trait Ops extends Any {
        type Resource
        def source: Resource
        implicit def sourceOps: CouldBe[Resource.Ops]#t[Resource]

        type Storage
        def storage: Storage
        implicit def storageOps: CouldBe[Storage.Ops]#t[Storage]

        final class ForItem(uri: Uri) {
          private[this] val handleAcquired: LockResult ~~> Try[Out] = {
            case LockResult.Acquired(outs) ⇒ Success {
              val sink = makeAcquiredSink(outs)
              source(uri).alsoToMat(sink)(Keep.right)
            }
          }

          private[this] val handleLockTry: LockResult ~~> Try[Out] =
            handleAcquired orElse handleFound

          /**
           * Store resource if it does not exist.
           */
          def apply(uri: Uri): Out =
            storage
              .tryLock(uri.toString)
              .flatMap(handleLockTry)
              .convertToEffect[Future]()
              .convertTo[Source[ByteString, Future[Done]]]
        }

        final def apply(uri: Uri) = new ForItem(uri)
      }
    }

    final implicit def cachingResourceOps[T: CachingResource](self: T): CachingResource.Ops = CachingResource[T](self)
  }

  def main0(args: Array[String]): Unit = {
    object i extends ResourcePackage with FallBackResourcePackage with CachingResourcePackage
      with TypeClassPackage
      with StoragePackage
      with Util
      with StorageMapPackage
    import i._
    import isi.std.io._
    import isi.akka._
    import isi.concurrent.Executors.{ SyncExecutor ⇒ sameThread }

    implicit def recordResource[rc: i.CouldBe[i.Storage.Ops]#t, rec <: HList](rec: rc :: rec): Resource.Ops = new Resource.Ops {
      val storage :: _ = rec


      final def apply(uri: Uri): Source[ByteString, NotUsed] = {
        val item = uri.toString

        val extractChannelFromUnlock: i.UnlockResult ⇒ Try[ReadableByteChannel] = {
          case i.UnlockResult.Unlocked ⇒ Failure(new Exception(s"not found: $uri"))
        }

        val extractChannelFromLock: i.LockResult ⇒ Try[ReadableByteChannel] = {
          case i.LockResult.Found(ins) ⇒ Success(ins)
          case i.LockResult.Acquired(outs) ⇒ for {
            _ ← Try { outs.close() }
            unlockResult ← storage tryUnlock item
            channel ← extractChannelFromUnlock(unlockResult)
          } yield channel
        }

        val trySource: Try[Source[ByteString, NotUsed]] = for {
          lockResult ← storage tryLock item
          channelIn ← extractChannelFromLock(lockResult)
          stream = channelIn.convertTo[Stream[ByteString]]
          source = stream.convertTo[Source[ByteString, NotUsed]]
        } yield source

        trySource.convertTo[Source[ByteString, NotUsed]]
      }
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
