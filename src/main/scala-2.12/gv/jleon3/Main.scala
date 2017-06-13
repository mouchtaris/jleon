package gv
package jleon3

import java.nio.file.{ Path ⇒ JPath, FileSystem ⇒ JFileSystem, FileAlreadyExistsException, NoSuchFileException }
import java.nio.channels.{
  //  ReadableByteChannel,
  WritableByteChannel
  //  ,Channels
}

import language.{
  postfixOps,
  implicitConversions
  //  ,higherKinds,
  //  existentials
}
import util.{ Try, Success, Failure }
import concurrent.{ Future, ExecutionContext, Await }
import concurrent.duration._

//import akka.stream.scaladsl.{ Source, Flow, Sink }
import akka.stream.{ Materializer, ActorMaterializer }
import akka.actor.{ ActorSystem }
//import akka.http.scaladsl.server.{ Route, Directives, Directive0 }
//import akka.http.scaladsl.{ Http }

import com.typesafe.config.{ Config ⇒ TSConfig, ConfigFactory ⇒ TSConfigFactory }
import com.typesafe.scalalogging.{ StrictLogging }

//import isi.convertible._
//import isi.std.conversions._
import isi.{ ~~> }

object Main extends StrictLogging {
  app ⇒

  final case class Main() {
    logger info "Creating Actor System"
    implicit val actorSystem: ActorSystem = ActorSystem("leon")

    logger info "Creating Stream Materializer"
    implicit val materializer: Materializer = ActorMaterializer()

    logger info "Loading Application Config"
    val config: TSConfig = TSConfigFactory.defaultApplication

    logger info "Acquiring File System"
    implicit val fileSystem = java.nio.file.FileSystems.getDefault

    logger info "Acquiring Execution Context"
    implicit val executionContext: ExecutionContext = materializer.executionContext

    logger info "Creating Storage Factory"
    val storageFactory = StorageFactory(config)

    //    logger info "Importing Storage Components"
    //    import storageFactory.{
    //      storageMap,
    //      storage
    //    }
    //
    private[this] def logFutureCompletion[T](msg: String): T ⇒ T =
      result ⇒ { logger info msg; result; }

    def shutdown(): Future[akka.actor.Terminated] = {
      implicit val safeEc = ExecutionContext.Implicits.global

      logger info "Terminating Actor System"
      actorSystem
        .terminate()
        .map(logFutureCompletion("  [OK] Actor System Terminated"))(safeEc)
    }
  }

  type FactorySourceOf[T] = { type t[a] = a ⇒ T }

  trait Config extends Any {
    type T <: TSConfig

    def self: T
    implicit def fileSystem: JFileSystem

    def path: String ⇒ JPath = (self getString _) andThen (fileSystem getPath _)
  }
  object Config {
    final type Aux[a <: TSConfig] = Config { type T = a }
    implicit def toCore[T <: TSConfig](config: Config.Aux[T]): T = config.self
  }

  implicit def TSConfigConfigFactory(implicit fs: JFileSystem): TSConfig ⇒ Config.Aux[TSConfig] =
    config ⇒ new Config {
      final type T = TSConfig
      val self: TSConfig = config
      val fileSystem: JFileSystem = fs
    }

  trait Path[T] extends Any {
    def self: T
    def addExt(filename: String): T
  }

  implicit class JPathPath(val self: JPath) extends AnyVal with Path[JPath] {
    def addExt(ext: String): JPath = self resolveSibling s"${self.getFileName}.$ext"
  }

  final case class StorageMap(base: JPath) {
    final case class forItem(item: String) {
      val storage: JPath = base resolveSibling item
      val lock: JPath = storage addExt StorageMap.EXT_LOCK
      val failure: JPath = storage addExt StorageMap.EXT_FAILURE
    }
  }

  object StorageMap {
    final val EXT_LOCK = "lock"
    final val EXT_FAILURE = "failed"
  }

  trait StoragePackage {

    sealed trait LockResult

    object LockResult {
      final case class Acquired(channel: WritableByteChannel) extends LockResult

      final case class Locked(item: String, cause: FileAlreadyExistsException)
        extends Exception(s"item is locked: $item", cause) with LockResult

      final case class Failed(item: String)
        extends Exception(s"item is failed: $item") with LockResult
    }
  }

  trait Storage {
    this: StoragePackage ⇒

    import java.nio.file.{ Files, StandardOpenOption ⇒ oopt }

    type StorageMap <: app.StorageMap
    val storageMap: StorageMap

    private[this]type Map = storageMap.forItem

    private[this] object withMap {
      val nothing: Try[LockResult] = Failure(new Exception("nothing done yet"))
      def pf[T](t: ⇒ T): Any ~~> T = { case _ ⇒ t }
    }

    private[this] final case class withMap(map: Map) {
      import withMap.{ pf, nothing }

      val getLock: Try[LockResult] = Try {
        Files newByteChannel (map.lock, oopt.CREATE_NEW) close ()
      } flatMap { _ ⇒
        Failure(new NoSuchFileException(map.lock.toString))
      } recover {
        case ex: FileAlreadyExistsException ⇒ LockResult.Locked(map.item, ex)
      }

      val getFailure: Try[LockResult] =
        if (Files exists map.failure)
          Success(LockResult.Failed(map.item))
        else
          Failure(new NoSuchFileException(map.failure.toString))

      val getStorage: Try[LockResult] = Try {
        LockResult.Acquired(Files newByteChannel map.storage)
      }

      val tryLock: Try[LockResult] =
        nothing recoverWith pf(getFailure) recoverWith pf(getLock) recoverWith pf(getStorage)
    }

    final val tryLock: String ⇒ Try[LockResult] =
      (storageMap forItem _) andThen withMap.apply andThen (_ tryLock)
  }

  trait StorageFactory {
    type Config <: app.Config
    val config: Config
    //    require(Option(config).isDefined)
    println(config)
    //
    //    final val storageMap: StorageMap = StorageMap(
    //      base = config.fileSystem.getPath("lol") // config path StorageFactory.BASE_PATH
    //    )
    //
    //    final val storage: Storage = new Storage with StoragePackage {
    //      final type StorageMap = app.StorageMap
    //      final val storageMap: StorageMap = StorageFactory.this.storageMap
    //    }
  }
  object StorageFactory {
    val BASE_PATH = "basePath"

    def apply[T <: TSConfig, S: FactorySourceOf[Config.Aux[T]]#t](configSource: S): StorageFactory =
      new {
        final val config: Config.Aux[T] = configSource
      } with StorageFactory {
        final type Config = Config.Aux[T]
      }
  }

  def main(args: Array[String]): Unit = {
    import ExecutionContext.Implicits.global

    val ready =
      Future {
        println("Hello this is leon")
      } map { _ ⇒
        Main()
      } flatMap { main ⇒
        main shutdown ()
      } map { _ ⇒
        println("Byte byte leon")
      }
    Await.result(ready, 5 seconds)
  }

}
