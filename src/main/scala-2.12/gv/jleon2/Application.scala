package gv
package jleon2

//import java.nio.ByteBuffer
//import java.nio.channels.WritableByteChannel

//import language.{ implicitConversions, postfixOps, higherKinds }
//import concurrent.{
//duration, Future, Await,
//ExecutionContext
//}
//import util.Try
//import ExecutionContext.Implicits.global

//import com.typesafe.scalalogging.{ StrictLogging }

//import scalaz.{ Monad }

//import gv.{ jleon2 ⇒ leon }
//import leon.model.facade.ExecutionContexts

object Application {
  app ⇒

  trait Mirror extends model.mirror.Mirror {
    final type Prefix = String

    val Handler: model.mirror.Handler.tp[Prefix] = (_: String) ⇒ ??? //
    final type Handler = Handler.type

    val HandlerRepository: isi.Repository.tp[Prefix, Handler] = (_: String) ⇒ ??? //
    final type HandlerRepository = HandlerRepository.type

    val HandlerFactory: isi.Factory.tp[String, Handler] = (_: String) ⇒ ??? //
    final type HandlerFactory = HandlerFactory.type

    val HandlerRepositoryFactory: isi.Factory.tp[String, HandlerRepository] = (_: String) ⇒ ??? //
    final type HandlerRepositoryFactory = HandlerRepositoryFactory.type
  }

  val Mirror = new Mirror {}

  //  object Uri {
  //    trait Types extends leon.model.slice.Uri.Types {
  //      final type Uri = app.Uri
  //    }
  //    object Types extends Types
  //  }
  //  final case class Uri(uri: String, boundToFail: Boolean) {
  //    import java.net.{ URI ⇒ Java }
  //    import akka.http.scaladsl.model.{ Uri ⇒ Akka }
  //    implicit def javaUri: Java = Java create uri
  //    implicit def akkaUri: Akka = Akka apply uri
  //
  //    def map[S](f: Uri ⇒ S): S = f(this)
  //    def future: Future[this.type] =
  //      if (boundToFail)
  //        Future failed new RuntimeException("Request was bound to fail")
  //      else
  //        Future successful this
  //  }
  //
  //  trait BaseHandler {
  //    // Input
  //    type Request <: Application.Uri
  //    // Output
  //    type Result
  //
  //    val saveit: Boolean ⇒ Boolean
  //    def success: Application.Uri ⇒ Future[Result]
  //
  //    def process[R <: Request]: R ⇒ Application.Uri =
  //      req ⇒ req copy (boundToFail = saveit(req.boundToFail))
  //
  //    def handle(request: Request): Future[Result] =
  //      process(request).future flatMap success
  //  }
  //  trait CrappyHandler {
  //    this: BaseHandler ⇒
  //    val saveit: Boolean ⇒ Boolean = Predef identity
  //  }
  //  trait LokiHandler {
  //    this: BaseHandler ⇒
  //    val saveit: Boolean ⇒ Boolean = !_
  //  }
  //
  //  object Error {
  //    final case class Mirror() extends leon.model.error.Mirror {
  //      def apply[F[_]: Monad, Result](result: F[Result]): F[Result] =
  //        result
  //    }
  //    final case class Storage() extends leon.model.error.Storage {
  //      def apply[F[_]: Monad, Result](result: F[Result]): F[Result] =
  //        result
  //    }
  //    trait Types extends leon.model.slice.Error.Types {
  //      final type Error = app.Error
  //    }
  //    object Types extends Types
  //  }
  //
  //  final case class Error() extends leon.model.error.Error {
  //    type MirrorHandler = Error.Mirror
  //    type StorageHandler = Error.Storage
  //
  //    implicit def mirror: MirrorHandler = Error.Mirror()
  //    implicit def storage: StorageHandler = Error.Storage()
  //  }
  //
  //  object Mirror {
  //    trait Types extends leon.model.slice.Mirror.Types with Uri.Types {
  //      final type Mirror = app.Mirror
  //    }
  //
  //    import shapeless.syntax.singleton.mkSingletonOps
  //    val mostlyOk = "mostlyOk".narrow
  //    val fromHell = "fromHell".narrow
  //  }
  //  final case class Mirror() extends leon.model.mirror.Mirror {
  //    import leon.model.mirror
  //
  //    object Handler {
  //      trait Types {
  //        final type Request = Uri.Types.Uri
  //        final type Result = leon.model.mirror.HandlingResult
  //      }
  //      object Types extends Types
  //
  //      trait Base extends leon.model.mirror.Handler with Types with BaseHandler {
  //        def success: Application.Uri ⇒ Future[Result] =
  //          request ⇒
  //            Future successful mirror.HandlingResult.Found {
  //              import isi.convertible._
  //              import isi.std.conversions._
  //              import isi.std.io.ByteSource._
  //              import java.nio.channels.ReadableByteChannel
  //
  //              s"Hello Bob ${request}"
  //                .convertTo[Array[Byte]]
  //                .convertTo[ReadableByteChannel]
  //            }
  //      }
  //
  //      trait Loki extends Base with LokiHandler
  //      trait Crappy extends Base with CrappyHandler
  //
  //      def Crappy: Crappy = new Crappy {}
  //      def Loki: Loki = new Loki {}
  //    }
  //
  //    type Handler = Handler.Base
  //    type Prefix = String
  //
  //    import Mirror.{ mostlyOk, fromHell }
  //    def apply(prefix: Prefix): Future[Handler] = Future successful prefix map {
  //      case `mostlyOk` ⇒ Handler.Crappy
  //      case `fromHell` ⇒ Handler.Loki
  //    }
  //  }
  //
  //  object Storage {
  //    trait Types extends leon.model.slice.Storage.Types with Uri.Types {
  //      final type Storage = app.Storage
  //    }
  //  }
  //  final case class Storage() extends leon.model.storage.Storage {
  //    import model.storage
  //
  //    type Request = Uri
  //    type LockResult = storage.LockResult
  //
  //    def tryLock(request: Request): Future[LockResult] = Future successful request flatMap {
  //      request ⇒
  //        Future successful storage.LockResult.Acquired(new WritableByteChannel {
  //          def write(src: ByteBuffer): Int = {
  //            println(s"$request: hahahaha: ${src.toString}")
  //            val written = src.remaining() + 1
  //            src.position(src.limit())
  //            written
  //          }
  //          def isOpen: Boolean = true
  //          def close(): Unit = ()
  //        })
  //    }
  //  }
  //
  //  final case class ExecutionContexts() extends leon.model.facade.ExecutionContexts {
  //    import isi.std.conversions._
  //    implicit val RequestProcessing: ExecutionContext =
  //      // these two options create some weird deadlock
  //      //java.util.concurrent.Executors.newFixedThreadPool(16)
  //      //java.util.concurrent.Executors.newSingleThreadExecutor()
  //      java.util.concurrent.Executors.newWorkStealingPool(16)
  //  }
  //
  //  trait Slices extends AnyRef
  //      with leon.model.slice.Mirror
  //      with Mirror.Types
  //      with leon.model.slice.Storage
  //      with Storage.Types
  //      with leon.model.slice.Error
  //      with Error.Types {
  //    val Mirror = Application.Mirror()
  //    val Storage = Application.Storage()
  //    val Error = Application.Error()
  //    val ExecutionContexts = Application.ExecutionContexts()
  //  }
  //
  //  final case class Leon() extends leon.model.JLeon with Slices
  //
  //  def main(args: Array[String]): Unit = {
  //
  //    object main extends StrictLogging {
  //      val Logger = logger
  //    }
  //    import main.{ Logger ⇒ logger }
  //
  //    logger info s"creating leon"
  //    val leon = new Leon
  //
  //    val maximumPatience = duration.Duration(3, duration.SECONDS)
  //    logger info s"setting maximum patience to $maximumPatience"
  //
  //    logger info "creating the future"
  //    val futureResult = {
  //      val uri = Uri("/bohos/me/a/las/rajas", boundToFail = false)
  //      logger info s"uri set to $uri"
  //
  //      val prefix = Mirror.mostlyOk
  //      logger info s"prefix set to $prefix"
  //
  //      logger info "going for serverRequest..."
  //      leon serveRequest (prefix, uri) andThen { case a ⇒ println(a) }
  //    }
  //
  //    logger info "awaiting result to be ready (with maximum patience)"
  //    Await.ready(futureResult, maximumPatience)
  //    logger info "result was ready"
  //  }

}

/**
 *
 * Warning:scalac: package gv {
 * package jleon2 {
 * import scala.language.implicitConversions;
 * import scala.concurrent.{Future, ExecutionContext, Await, duration};
 * import scala.util.Try;
 * import gv.{jleon2=>leon};
 * object Application extends scala.AnyRef {
 * def <init>(): gv.jleon2.Application.type = {
 * Application.super.<init>();
 * ()
 * };
 * final type MirrorPrefix = String;
 * final case class Mirror extends AnyRef with gv.jleon2.model.mirror.Mirror with Product with Serializable {
 * def <init>(): gv.jleon2.Application.Mirror = {
 * Mirror.super.<init>();
 * ()
 * };
 * import gv.jleon2.model.mirror;
 * override type Prefix = String;
 * case class Handler extends AnyRef with gv.jleon2.model.mirror.Handler with Product with Serializable {
 * def <init>(): Mirror.this.Handler = {
 * Handler.super.<init>();
 * ()
 * };
 * case class Request extends AnyRef with Product with Serializable {
 * def <init>(): Handler.this.Request = {
 * Request.super.<init>();
 * ()
 * };
 * <synthetic> def copy(): Handler.this.Request = new Request();
 * override <synthetic> def productPrefix: String = "Request";
 * <synthetic> def productArity: Int = 0;
 * <synthetic> def productElement(x$1: Int): Any = x$1 match {
 * case _ => throw new IndexOutOfBoundsException(x$1.toString())
 * };
 * override <synthetic> def productIterator: Iterator[Any] = scala.runtime.ScalaRunTime.typedProductIterator[Any](Request.this);
 * <synthetic> def canEqual(x$1: Any): Boolean = x$1.$isInstanceOf[Handler.this.Request]();
 * override <synthetic> def hashCode(): Int = scala.runtime.ScalaRunTime._hashCode(Request.this);
 * override <synthetic> def toString(): String = scala.runtime.ScalaRunTime._toString(Request.this);
 * override <synthetic> def equals(x$1: Any): Boolean = x$1 match {
 * case (_: Handler.this.Request) => true
 * case _ => false
 * }.&&(x$1.asInstanceOf[Handler.this.Request].canEqual(Request.this))
 * };
 * <synthetic> object Request extends scala.runtime.AbstractFunction0[Handler.this.Request] with Serializable {
 * def <init>(): Handler.this.Request.type = {
 * Request.super.<init>();
 * ()
 * };
 * final override <synthetic> def toString(): String = "Request";
 * case <synthetic> def apply(): Handler.this.Request = new Request();
 * case <synthetic> def unapply(x$0: Handler.this.Request): Boolean = if (x$0.==(null))
 * false
 * else
 * true
 * };
 * type Result = gv.jleon2.model.mirror.HandlingResult;
 * def handle(request: Handler.this.Request): scala.concurrent.Future[Handler.this.Result] = {
 * scala.concurrent.Future.successful[gv.jleon2.model.mirror.HandlingResult.Found](gv.jleon2.model.mirror.HandlingResult.Found.apply({
 * import isi.io.ByteSource;
 * import isi.convertible._;
 * import isi.std.conversions._;
 * import isi.std.io.ByteSource._;
 * import java.nio.ByteBuffer;
 * import java.nio.channels.ReadableByteChannel;
 * isi.convertible.`package`.Convertible[Array[Byte]](scala.Array.empty[Byte]((ClassTag.Byte: scala.reflect.ClassTag[Byte]))).convertTo[java.nio.channels.ReadableByteChannel](isi.std.conversions.`package`.T: ByteSource ~=> ReadableByteChannel[Array[Byte]](isi.std.io.ByteSource.T ~=> ReadableByteChanel: ByteSource[Array[Byte]](isi.convertible.`package`.A ~=> B ~=> C[Array[Byte], java.nio.ByteBuffer, java.nio.channels.ReadableByteChannel](isi.std.conversions.`package`.Byte[Array] ~=> ByteBuffer, isi.std.conversions.`package`.T: ByteSource ~=> ReadableByteChannel[java.nio.ByteBuffer](isi.std.io.ByteSource.ByteBuffer: ByteSource)))));
 * scala.Predef.???
 * }));
 * scala.Predef.???
 * };
 * <synthetic> def copy(): Mirror.this.Handler = new Handler();
 * override <synthetic> def productPrefix: String = "Handler";
 * <synthetic> def productArity: Int = 0;
 * <synthetic> def productElement(x$1: Int): Any = x$1 match {
 * case _ => throw new IndexOutOfBoundsException(x$1.toString())
 * };
 * override <synthetic> def productIterator: Iterator[Any] = scala.runtime.ScalaRunTime.typedProductIterator[Any](Handler.this);
 * <synthetic> def canEqual(x$1: Any): Boolean = x$1.$isInstanceOf[Mirror.this.Handler]();
 * override <synthetic> def hashCode(): Int = scala.runtime.ScalaRunTime._hashCode(Handler.this);
 * override <synthetic> def toString(): String = scala.runtime.ScalaRunTime._toString(Handler.this);
 * override <synthetic> def equals(x$1: Any): Boolean = x$1 match {
 * case (_: Mirror.this.Handler) => true
 * case _ => false
 * }.&&(x$1.asInstanceOf[Mirror.this.Handler].canEqual(Handler.this))
 * };
 * <synthetic> object Handler extends scala.runtime.AbstractFunction0[Mirror.this.Handler] with Serializable {
 * def <init>(): Mirror.this.Handler.type = {
 * Handler.super.<init>();
 * ()
 * };
 * final override <synthetic> def toString(): String = "Handler";
 * case <synthetic> def apply(): Mirror.this.Handler = new Handler();
 * case <synthetic> def unapply(x$0: Mirror.this.Handler): Boolean = if (x$0.==(null))
 * false
 * else
 * true
 * };
 * def apply(prefix: Mirror.this.Prefix): scala.concurrent.Future[Mirror.this.Handler] = scala.Predef.???;
 * <synthetic> def copy(): gv.jleon2.Application.Mirror = new Mirror();
 * override <synthetic> def productPrefix: String = "Mirror";
 * <synthetic> def productArity: Int = 0;
 * <synthetic> def productElement(x$1: Int): Any = x$1 match {
 * case _ => throw new IndexOutOfBoundsException(x$1.toString())
 * };
 * override <synthetic> def productIterator: Iterator[Any] = scala.runtime.ScalaRunTime.typedProductIterator[Any](Mirror.this);
 * <synthetic> def canEqual(x$1: Any): Boolean = x$1.$isInstanceOf[gv.jleon2.Application.Mirror]();
 * override <synthetic> def hashCode(): Int = scala.runtime.ScalaRunTime._hashCode(Mirror.this);
 * override <synthetic> def toString(): String = scala.runtime.ScalaRunTime._toString(Mirror.this);
 * override <synthetic> def equals(x$1: Any): Boolean = x$1 match {
 * case (_: gv.jleon2.Application.Mirror) => true
 * case _ => false
 * }
 * };
 * <synthetic> object Mirror extends scala.runtime.AbstractFunction0[gv.jleon2.Application.Mirror] with Serializable {
 * def <init>(): gv.jleon2.Application.Mirror.type = {
 * Mirror.super.<init>();
 * ()
 * };
 * final override <synthetic> def toString(): String = "Mirror";
 * case <synthetic> def apply(): gv.jleon2.Application.Mirror = new Mirror();
 * case <synthetic> def unapply(x$0: gv.jleon2.Application.Mirror): Boolean = if (x$0.==(null))
 * false
 * else
 * true;
 * <synthetic> private def readResolve(): Object = gv.jleon2.Application.Mirror
 * }
 * }
 * }
 * }
 */
