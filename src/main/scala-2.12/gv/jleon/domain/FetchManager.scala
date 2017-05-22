package gv.jleon
package domain

import akka.{ NotUsed }
import akka.util.{ ByteString }
import akka.stream.scaladsl.{ Source }

import shapeless.{ HNil, :: }

trait FetchManager extends Any {
  def mirrors: MirrorRepository
  def storage: LockingStorage

  private[this] def lock(uri: Uri): Try[Unit] =
    storage.lock(uri) map (_.close())

  private[this] def getMirrors(implicit prefix: Mirror.Prefix): Try[IndexedSeq[Mirror :: Fetch :: HNil]] =
    mirrors.self get prefix match {
      case Some(ms) ⇒ Success { ms }
      case None     ⇒ Failure { new NoSuchElementException(s"No mirror for prefix: $prefix") }
    }

  private[this] def getMirror(mirrorIndex: Int)(implicit prefix: Mirror.Prefix): Try[Mirror :: Fetch :: HNil] =
    getMirrors flatMap { mirrors ⇒
      if (mirrors isDefinedAt mirrorIndex)
        Success { mirrors(mirrorIndex) }
      else
        Failure { new IndexOutOfBoundsException(s"Mirror index: $mirrorIndex") }
    }

  def fetch(mirrorIndex: Int, uri: Uri)(implicit prefix: Mirror.Prefix): Try[Source[ByteString, NotUsed]] = {
    for {
      _ ← lock(uri)
      mirror :: fetch :: HNil ← getMirror(mirrorIndex)
      bytes = fetch(mirror urlFor uri.path)
    } yield bytes
  }

}

object FetchManager {

}
