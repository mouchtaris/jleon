package gv.jleon

import java.nio.file.{ Files, StandardOpenOption }

import scala.language.{ implicitConversions }

import shapeless.{ HNil, :: }

import gv.jleon.{ Path ⇒ jPath }
import `type`.{ TaggedType }
import crypto.Digestion.SHA512

import Storage._

final case class Storage(
    basePath: Path.t
) {
  private[this] def storagePath(uri: Uri): Path.t =
    basePath resolve SHA512.hexDigest(uri.toString)

  def fetch(uri: Uri): Try[ReadableByteChannel] =
    nonFatalCatch
      .withTry {
        val storagePath: Path = this.storagePath(uri)
        if (Files.exists(storagePath))
          Success { Files.newByteChannel(storagePath, StandardOpenOption.READ) }
        else
          Failure { new IOException(s"File not found: $storagePath") }
      }
      .flatten
}

object Storage {

  final implicit object Path extends TaggedType[jPath]

  trait Interpretation[T] extends Any {
    final type Self = T

    def basePath(self: Self): Path.t
  }

  final implicit def apply[T: Interpretation](self: T): Storage = {
    val i: Interpretation[T] = implicitly
    Storage(
      basePath = i basePath self
    )
  }

  final implicit def recordI = new Interpretation[Path.t :: HNil] {
    override def basePath(self: Self): Path.t = self match {
      case p :: _ ⇒ p
    }
  }

}
