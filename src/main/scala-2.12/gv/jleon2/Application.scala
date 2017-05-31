package gv
package jleon2

import java.nio.ByteBuffer
import java.nio.channels.WritableByteChannel

import language.implicitConversions
import concurrent.{ duration, Future, Await, ExecutionContext }
import util.Try
import ExecutionContext.Implicits.global
import gv.{ jleon2 ⇒ leon }
import leon.model.facade.ExecutionContexts

object Application {

  final case class Uri(uri: String, boundToFail: Boolean) {
    import java.net.{ URI ⇒ Java }
    import akka.http.scaladsl.model.{ Uri ⇒ Akka }
    implicit def javaUri: Java = Java create uri
    implicit def akkaUri: Akka = Akka apply uri

    def map[S](f: Uri ⇒ S): S = f(this)
    def future: Future[this.type] =
      if (boundToFail)
        Future failed new RuntimeException("Request was bound to fail")
      else
        Future successful this
  }

  trait BaseHandler {
    // Input
    type Request <: Application.Uri
    // Output
    type Result

    val saveit: Boolean ⇒ Boolean
    def success: Application.Uri ⇒ Future[Result]

    def process[R <: Request]: R ⇒ Application.Uri =
      req ⇒ req copy (boundToFail = saveit(req.boundToFail))

    def handle(request: Request): Future[Result] =
      process(request).future flatMap success
  }
  trait CrappyHandler {
    this: BaseHandler ⇒
    val saveit: Boolean ⇒ Boolean = Predef identity
  }
  trait LokiHandler {
    this: BaseHandler ⇒
    val saveit: Boolean ⇒ Boolean = !_
  }

  object Types {
    import leon.model.slice

    trait uri extends slice.Uri.Types {
      final type Uri = Application.Uri
    }
    object uri extends uri
    type Uri = uri.Uri

    trait mirror extends slice.Mirror.Types with uri {
      thisSlice ⇒

      trait Handler extends leon.model.mirror.Handler with BaseHandler {
        // Inputs
        final type Request = thisSlice.Uri
        // Outputs
        final type Result = leon.model.mirror.HandlingResult
      }

      type SuperMirror = Mirror

      trait Mirror extends leon.model.mirror.Mirror {
        // Outputs
        final type Handler = thisSlice.Handler
      }
    }
    object mirror extends mirror
    type Mirror = mirror.Mirror

    trait storage extends slice.Storage.Types with uri
    object storage extends storage
    type Storage = storage.Storage
  }

  final case class Mirror() extends Types.Mirror {
    import leon.model.mirror

    private[this] object handlers {
      trait Base extends Types.mirror.Handler {
        def success: Application.Uri ⇒ Future[Result] =
          request ⇒
            Future successful mirror.HandlingResult.Found {
              import isi.convertible._
              import isi.std.conversions._
              import isi.std.io.ByteSource._
              import java.nio.channels.ReadableByteChannel

              s"Hello Bob ${request}"
                .convertTo[Array[Byte]]
                .convertTo[ReadableByteChannel]
            }

      }

      trait Loki extends Base with LokiHandler
      trait Crappy extends Base with CrappyHandler

      final case object Crappy extends Crappy
      final case object Loki extends Loki
    }

    def apply(prefix: Prefix): Future[Handler] = Future successful prefix map {
      case "mostylOk" ⇒ handlers.Crappy
      case "fromhell" ⇒ handlers.Loki
    }
  }

  //  final case class Storage() extends leon.model.storage.Storage {
  //    import model.storage
  //
  //    type Request = Uri
  //    type LockResult = storage.LockResult
  //
  //    def tryLock(request: Request): Future[LockResult] = Future successful request flatMap {
  //      case Uri(_, boundToFail) if boundToFail ⇒
  //        Requests.failed
  //      case _ ⇒ Future successful storage.LockResult.Acquired(new WritableByteChannel {
  //        def write(src: ByteBuffer): Int = {
  //          println(s"hahahaha: ${src.toString}")
  //          val written = src.remaining() + 1
  //          src.position(src.limit())
  //          written
  //        }
  //        def isOpen: Boolean = true
  //        def close(): Unit = ()
  //      })
  //    }
  //  }

  trait Slices extends AnyRef
      with Types.mirror
      with leon.model.slice.Mirror
      with leon.model.slice.Storage
      with leon.model.slice.Error
  {
    import leon.model.slice

    val Mirror = Application.Mirror()

    /**
     * As seen from class Leon, the missing signatures are as follows.
     *  For convenience, these are usable as stub implementations.
     */
    // Members declared in gv.jleon2.model.slice.Error
    val Error = ???

    // Members declared in gv.jleon2.model.JLeon
    val ExecutionContexts = ???


    // Members declared in gv.jleon2.model.slice.Storage
    implicit val Storage = ???
  }
  //
  //  final case class Error() extends leon.model.error.Error {
  //    type Storage
  //    type Mirror = this.type
  //    type Uri = this.type
  //
  //    type MirrorHandler = this.type
  //    type StorageHandler = this.type
  //
  //    def mirror: Error.this.type = ???
  //
  //    def storage: Error.this.type = ???
  //
  //  }

  final case class Leon() extends leon.model.JLeon with Slices {

    //    type Storage = Application.Storage
    //    type Error = Application.Error
    //
    //    val ExecutionContexts: ExecutionContexts = new ExecutionContexts {
    //      val RequestProcessing: ExecutionContext = global
    //    }
    //
    //    val Mirror: Mirror = Application.Mirror()
    //    val Storage: Storage = Application.Storage()
    //    val Error: Error = Application.Error()
  }

  //  object Implicits extends AnyRef
  //    with isi.std.conversions.ExecutionContextConversions
  //    with isi.std.conversions.UriConversions
  //  import Implicits._
  //

  //
  //  class Storage extends leon.model.storage.Storage {
  //    import leon.model.storage.LockResult
  //
  //    override type Request = Uri
  //
  //    override def tryLock(request: Uri): Future[LockResult] = Future successful {
  //      LockResult Failed new RuntimeException(s"no locking yet: $request")
  //    }
  //  }
  //
  //  class Mirror extends leon.model.mirror.Mirror
  //
  //  class MirrorRepository extends leon.model.mirror.MirrorRepository {
  //    override type Prefix = String
  //    override type Mirror = Application.Mirror
  //
  //    override def apply(prefix: Prefix): Future[Mirror] = prefix match {
  //      case "bohos" ⇒ Future successful new Mirror
  //      case _       ⇒ Future failed new NoSuchElementException(s"prefix poo: $prefix")
  //    }
  //  }
  //
  //  class JLeon extends leon.model.JLeon {
  //    import java.util.concurrent.Executors.{ newWorkStealingPool }
  //
  //    override type Storage = Application.Storage
  //    override object Storage extends Storage
  //
  //    override type MirrorRepository = Application.MirrorRepository
  //    override object MirrorRepository extends MirrorRepository
  //
  //    override object ExecutionContexts extends leon.model.facade.ExecutionContexts {
  //      override val RequestProcessing: ExecutionContext = newWorkStealingPool(16)
  //    }
  //  }
  //
  //  def main(args: Array[String]): Unit = {
  //
  //    val leon = new JLeon
  //
  //    Await.ready(
  //      leon.serveRequest("bohos", Uri("/bohos/me/a/las/rajas")),
  //      duration.Duration(3, duration.SECONDS)
  //    )
  //
  //  }

}

/**

Warning:scalac: package gv {
  package jleon2 {
    import scala.language.implicitConversions;
    import scala.concurrent.{Future, ExecutionContext, Await, duration};
    import scala.util.Try;
    import gv.{jleon2=>leon};
    object Application extends scala.AnyRef {
      def <init>(): gv.jleon2.Application.type = {
        Application.super.<init>();
        ()
      };
      final type MirrorPrefix = String;
      final case class Mirror extends AnyRef with gv.jleon2.model.mirror.Mirror with Product with Serializable {
        def <init>(): gv.jleon2.Application.Mirror = {
          Mirror.super.<init>();
          ()
        };
        import gv.jleon2.model.mirror;
        override type Prefix = String;
        case class Handler extends AnyRef with gv.jleon2.model.mirror.Handler with Product with Serializable {
          def <init>(): Mirror.this.Handler = {
            Handler.super.<init>();
            ()
          };
          case class Request extends AnyRef with Product with Serializable {
            def <init>(): Handler.this.Request = {
              Request.super.<init>();
              ()
            };
            <synthetic> def copy(): Handler.this.Request = new Request();
            override <synthetic> def productPrefix: String = "Request";
            <synthetic> def productArity: Int = 0;
            <synthetic> def productElement(x$1: Int): Any = x$1 match {
              case _ => throw new IndexOutOfBoundsException(x$1.toString())
            };
            override <synthetic> def productIterator: Iterator[Any] = scala.runtime.ScalaRunTime.typedProductIterator[Any](Request.this);
            <synthetic> def canEqual(x$1: Any): Boolean = x$1.$isInstanceOf[Handler.this.Request]();
            override <synthetic> def hashCode(): Int = scala.runtime.ScalaRunTime._hashCode(Request.this);
            override <synthetic> def toString(): String = scala.runtime.ScalaRunTime._toString(Request.this);
            override <synthetic> def equals(x$1: Any): Boolean = x$1 match {
  case (_: Handler.this.Request) => true
  case _ => false
}.&&(x$1.asInstanceOf[Handler.this.Request].canEqual(Request.this))
          };
          <synthetic> object Request extends scala.runtime.AbstractFunction0[Handler.this.Request] with Serializable {
            def <init>(): Handler.this.Request.type = {
              Request.super.<init>();
              ()
            };
            final override <synthetic> def toString(): String = "Request";
            case <synthetic> def apply(): Handler.this.Request = new Request();
            case <synthetic> def unapply(x$0: Handler.this.Request): Boolean = if (x$0.==(null))
              false
            else
              true
          };
          type Result = gv.jleon2.model.mirror.HandlingResult;
          def handle(request: Handler.this.Request): scala.concurrent.Future[Handler.this.Result] = {
            scala.concurrent.Future.successful[gv.jleon2.model.mirror.HandlingResult.Found](gv.jleon2.model.mirror.HandlingResult.Found.apply({
              import isi.io.ByteSource;
              import isi.convertible._;
              import isi.std.conversions._;
              import isi.std.io.ByteSource._;
              import java.nio.ByteBuffer;
              import java.nio.channels.ReadableByteChannel;
              isi.convertible.`package`.Convertible[Array[Byte]](scala.Array.empty[Byte]((ClassTag.Byte: scala.reflect.ClassTag[Byte]))).convertTo[java.nio.channels.ReadableByteChannel](isi.std.conversions.`package`.T: ByteSource ~=> ReadableByteChannel[Array[Byte]](isi.std.io.ByteSource.T ~=> ReadableByteChanel: ByteSource[Array[Byte]](isi.convertible.`package`.A ~=> B ~=> C[Array[Byte], java.nio.ByteBuffer, java.nio.channels.ReadableByteChannel](isi.std.conversions.`package`.Byte[Array] ~=> ByteBuffer, isi.std.conversions.`package`.T: ByteSource ~=> ReadableByteChannel[java.nio.ByteBuffer](isi.std.io.ByteSource.ByteBuffer: ByteSource)))));
              scala.Predef.???
            }));
            scala.Predef.???
          };
          <synthetic> def copy(): Mirror.this.Handler = new Handler();
          override <synthetic> def productPrefix: String = "Handler";
          <synthetic> def productArity: Int = 0;
          <synthetic> def productElement(x$1: Int): Any = x$1 match {
            case _ => throw new IndexOutOfBoundsException(x$1.toString())
          };
          override <synthetic> def productIterator: Iterator[Any] = scala.runtime.ScalaRunTime.typedProductIterator[Any](Handler.this);
          <synthetic> def canEqual(x$1: Any): Boolean = x$1.$isInstanceOf[Mirror.this.Handler]();
          override <synthetic> def hashCode(): Int = scala.runtime.ScalaRunTime._hashCode(Handler.this);
          override <synthetic> def toString(): String = scala.runtime.ScalaRunTime._toString(Handler.this);
          override <synthetic> def equals(x$1: Any): Boolean = x$1 match {
  case (_: Mirror.this.Handler) => true
  case _ => false
}.&&(x$1.asInstanceOf[Mirror.this.Handler].canEqual(Handler.this))
        };
        <synthetic> object Handler extends scala.runtime.AbstractFunction0[Mirror.this.Handler] with Serializable {
          def <init>(): Mirror.this.Handler.type = {
            Handler.super.<init>();
            ()
          };
          final override <synthetic> def toString(): String = "Handler";
          case <synthetic> def apply(): Mirror.this.Handler = new Handler();
          case <synthetic> def unapply(x$0: Mirror.this.Handler): Boolean = if (x$0.==(null))
            false
          else
            true
        };
        def apply(prefix: Mirror.this.Prefix): scala.concurrent.Future[Mirror.this.Handler] = scala.Predef.???;
        <synthetic> def copy(): gv.jleon2.Application.Mirror = new Mirror();
        override <synthetic> def productPrefix: String = "Mirror";
        <synthetic> def productArity: Int = 0;
        <synthetic> def productElement(x$1: Int): Any = x$1 match {
          case _ => throw new IndexOutOfBoundsException(x$1.toString())
        };
        override <synthetic> def productIterator: Iterator[Any] = scala.runtime.ScalaRunTime.typedProductIterator[Any](Mirror.this);
        <synthetic> def canEqual(x$1: Any): Boolean = x$1.$isInstanceOf[gv.jleon2.Application.Mirror]();
        override <synthetic> def hashCode(): Int = scala.runtime.ScalaRunTime._hashCode(Mirror.this);
        override <synthetic> def toString(): String = scala.runtime.ScalaRunTime._toString(Mirror.this);
        override <synthetic> def equals(x$1: Any): Boolean = x$1 match {
          case (_: gv.jleon2.Application.Mirror) => true
          case _ => false
        }
      };
      <synthetic> object Mirror extends scala.runtime.AbstractFunction0[gv.jleon2.Application.Mirror] with Serializable {
        def <init>(): gv.jleon2.Application.Mirror.type = {
          Mirror.super.<init>();
          ()
        };
        final override <synthetic> def toString(): String = "Mirror";
        case <synthetic> def apply(): gv.jleon2.Application.Mirror = new Mirror();
        case <synthetic> def unapply(x$0: gv.jleon2.Application.Mirror): Boolean = if (x$0.==(null))
          false
        else
          true;
        <synthetic> private def readResolve(): Object = gv.jleon2.Application.Mirror
      }
    }
  }
}
  **/
