package gv
package jleon4

//import java.net.{ URI ⇒ JUri }

import java.nio.{ ByteBuffer }
//import java.nio.file.{ Path ⇒ JPath, FileSystem ⇒ JFileSystem, FileAlreadyExistsException, NoSuchFileException }
//import java.nio.channels.{ ReadableByteChannel, WritableByteChannel, Channels }

//import language.{ postfixOps, implicitConversions, higherKinds, existentials }
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

  trait Resource {
    def apply(uri: Uri): Source1[ByteString]
  }

  trait FallBackResource extends Resource {
    val fallBack: Resource
    def applyImpl(uri: Uri): Source1[ByteString]

    final def apply(uri: Uri): Source1[ByteString] = applyImpl(uri) recoverWithRetries (1, pf(fallBack(uri)))
  }

  trait CachingResource {
    type Resource
    val source: Resource
    implicit val sourceOps: CouldBe[app.Resource]#t[Resource]
    type Storage
    val storage: Storage
    implicit val storageOps: CouldBe[Storage.Ops]#t[Storage]

    final type Out = Source[ByteString, Future[Done]]

    import isi.std.io._
    import isi.akka._
    private[this] val handleFound: LockResult ~~> Try[Out] = {
      case LockResult.Found(ins) ⇒ Success {
        ins
          .convertTo[Stream[ByteString]]
          .convertTo[Source[ByteString, NotUsed]]
          .mapMaterializedValue(_ ⇒ Future successful Done)
      }
    }

    private[this] def handleAcquired(uri: Uri): LockResult ~~> Try[Out] = {
      case LockResult.Acquired(outs) ⇒ Success {
        val sink: Sink[ByteString, Future[Int]] = outs
          .convertTo[Sink[ByteBuffer, Future[Int]]]
          .convertTo[Sink[ByteString, Future[Int]]]
        source(uri)
          .alsoToMat(sink)(Keep.right)
          .mapMaterializedValue(_.map(_ ⇒ Done)(isi.concurrent.Executors.SyncExecutor))
      }
    }

    private[this] val handleFailed: LockResult ~~> Try[Out] = {

      case fail @ LockResult.Failed(_) ⇒ Failure(fail)
    }

    private[this] val handleLocked: LockResult ~~> Try[Out] = {

      case fail @ LockResult.Locked(_, _) ⇒ Failure(fail)
    }

    private[this] val handleStandards: LockResult ~~> Try[Out] =
      handleFound orElse handleFailed orElse handleLocked

    private[this] def handleLockTry(uri: Uri): LockResult ~~> Try[Out] =
      handleAcquired(uri) orElse handleStandards

    /**
     * Store resource if it does not exist.
     */
    final def apply(uri: Uri): Source[ByteString, Future[Done]] =
      storage
        .tryLock(uri.toString)
        .flatMap(handleLockTry(uri))
        .convertToEffect[Future]()
        .convertTo[Source[ByteString, Future[Done]]]
  }

  def main(args: Array[String]): Unit = {
    val main = Main()
    val sto = main.factory.storageFactory.storage

    println {
      (1 to 2) map (_ ⇒ sto.tryLock("mani"))
    }
  }

}
