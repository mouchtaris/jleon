package gv

import java.{ io ⇒ jio }
import java.nio.{ file ⇒ jfile }
import java.nio.{ channels ⇒ jchannels }
import java.{ net ⇒ jnet }

import scala.{ util ⇒ sutil }
import scala.{ concurrent ⇒ sconcurrent }

import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }

package object jleon extends stdconv {
  protected[jleon]type IOException = jio.IOException

  protected[jleon]type ReadableByteChannel = jchannels.ReadableByteChannel

  protected[jleon]type Path = jfile.Path
  protected[jleon] object Path {
    def apply(uri: jnet.URI): Path = jfile.Paths.get(uri)
    def apply(s: String): Path = this(jnet.URI.create(s))
    def apply(uri: Uri): Path = this(uri: jnet.URI)
  }
  protected[jleon] object File {
    def exists(p: Path): Boolean = jfile.Files.exists(p)
    def exists(p: String): Boolean = exists(Path(p))
  }

  protected[jleon] final def nonFatalCatch[T]: sutil.control.Exception.Catch[T] = sutil.control.Exception.nonFatalCatch

  protected[jleon]type Try[T] = sutil.Try[T]
  protected[jleon] val Try = sutil.Try
  protected[jleon]type Success[T] = sutil.Success[T]
  protected[jleon] val Success = sutil.Success
  protected[jleon]type Failure[T] = sutil.Failure[T]
  protected[jleon] val Failure = sutil.Failure

  protected[jleon]type Future[T] = sconcurrent.Future[T]
  protected[jleon] val Future = sconcurrent.Future
  protected[jleon]type ExecutionContext = sconcurrent.ExecutionContext

  protected[jleon]type Uri = AkkaUri
  protected[jleon] val Uri = AkkaUri

  type Config = Config.Config

  final implicit class FetchRepository(val self: Map[String, Fetch]) extends AnyVal {
    @inline def apply(s: String): Option[Fetch] = self get s
  }
}
