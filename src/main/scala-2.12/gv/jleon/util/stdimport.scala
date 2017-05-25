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

  final type IOException = jio.IOException

  final type ReadableByteChannel = jchannels.ReadableByteChannel

  final type Path = jfile.Path
  final object Path {
    def apply(uri: jnet.URI): Path = jfile.Paths.get(uri)
    def apply(s: String): Path = this(jnet.URI.create(s))
    def apply(uri: Uri): Path = this(uri: jnet.URI)
  }
  final object File {
    val CREATE_NEW = jfile.StandardOpenOption.CREATE_NEW
    val CREATE = jfile.StandardOpenOption.CREATE
    val WRITE = jfile.StandardOpenOption.WRITE

    def exists(p: Path): Boolean = jfile.Files.exists(p)
    def exists(p: String): Boolean = exists(Path(p))

    def createNew(p: Path): Try[ReadableByteChannel] = Try {
      jfile.Files.newByteChannel(p, CREATE_NEW, WRITE)
    }

    def create(p: Path): Try[ReadableByteChannel] = Try {
      jfile.Files.newByteChannel(p, CREATE, WRITE)
    }

    def delete(p: Path): Boolean =
      jfile.Files.deleteIfExists(p)
  }

  final val UTF_8 = java.nio.charset.StandardCharsets.UTF_8

  final def nonFatalCatch[T]: sutil.control.Exception.Catch[T] = sutil.control.Exception.nonFatalCatch

  final type Try[T] = sutil.Try[T]
  final val Try = sutil.Try
  final type Success[T] = sutil.Success[T]
  final val Success = sutil.Success
  final type Failure[T] = sutil.Failure[T]
  final val Failure = sutil.Failure

  final type Future[T] = sconcurrent.Future[T]
  final val Future = sconcurrent.Future
  final type ExecutionContext = sconcurrent.ExecutionContext

  final type Uri = AkkaUri
  final val Uri = AkkaUri

}
