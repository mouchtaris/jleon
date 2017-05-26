package gv.jleon
package domain

import java.nio.file.{ Files, StandardOpenOption }

import scala.language.{ implicitConversions }

import shapeless.{ HNil, :: }

import gv.jleon.{ Path ⇒ jPath }

import `type`.{ TaggedType }

import crypto.Digestion.SHA512

final case class Storage(
    basePath: Storage.Path
) {
  def storagePath(uri: Uri): Storage.Path =
    basePath resolve SHA512.hexDigest(uri.toString)

  def storagePathWithExt(uri: Uri, ext: String): Storage.Path = {
    val storagePath: Path = this.storagePath(uri)
    val name: Path = storagePath.getFileName
    val nameWithExt: String = s"$name.$ext"
    val pathWithExt: Path = storagePath resolveSibling nameWithExt
    pathWithExt
  }

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

  def write(uri: Uri): Sink[ByteString, Future[IOResult]] =
    FileIO.toPath(storagePath(uri))
}

object Storage extends AnyRef
    with StorageFactory {

  final implicit object Path extends TaggedType[jPath]
  final type Path = Path.t

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

  final implicit def recordI = new Interpretation[Path :: HNil] {
    override def basePath(self: Self): Path = self match {
      case p :: _ ⇒ p
    }
  }

}
