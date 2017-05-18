package gv.jleon
package storage

import java.nio.file.{ Files, StandardOpenOption }
import crypto.Digestion.SHA512

trait Ops[T] extends Any {
  type I = Interpretation[T]

  protected[this] def self: T

  def basePath(implicit i: I): Path = i basePath self

  private[this] def storagePath(uri: Uri)(implicit i: I): Path =
    basePath resolve SHA512.hexDigest(uri.toString)

  def fetch(uri: Uri)(implicit i: I): Try[ReadableByteChannel] =
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
