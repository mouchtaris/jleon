package gv.jleon
package util

import java.{ io ⇒ jio }
import java.nio.{ file ⇒ jfile }
import java.nio.{ channels ⇒ jchannels }
import java.{ net ⇒ jnet }

import scala.{ util ⇒ sutil }
import scala.{ concurrent ⇒ sconcurrent }

import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }

trait stdimport {

  type IOException = jio.IOException

  type ReadableByteChannel = jchannels.ReadableByteChannel

  type Path = jfile.Path
  object Path {
    def apply(uri: jnet.URI): Path = jfile.Paths.get(uri)
    def apply(s: String): Path = this(jnet.URI.create(s))
    def apply(uri: Uri): Path = this(uri: jnet.URI)
  }
  object File {
    val CREATE_NEW = jfile.StandardOpenOption.CREATE_NEW
    val WRITE = jfile.StandardOpenOption.WRITE

    def exists(p: Path): Boolean = jfile.Files.exists(p)
    def exists(p: String): Boolean = exists(Path(p))

    def createNew(p: Path): Try[ReadableByteChannel] = Try {
      jfile.Files.newByteChannel(p, CREATE_NEW, WRITE)
    }

    def delete(p: Path): Boolean =
      jfile.Files.deleteIfExists(p)
  }

  final def nonFatalCatch[T]: sutil.control.Exception.Catch[T] = sutil.control.Exception.nonFatalCatch

  type Try[T] = sutil.Try[T]
  val Try = sutil.Try
  type Success[T] = sutil.Success[T]
  val Success = sutil.Success
  type Failure[T] = sutil.Failure[T]
  val Failure = sutil.Failure

  type Future[T] = sconcurrent.Future[T]
  val Future = sconcurrent.Future
  type ExecutionContext = sconcurrent.ExecutionContext

  type Uri = AkkaUri
  val Uri = AkkaUri

}
