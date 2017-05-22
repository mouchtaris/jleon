package gv.jleon
package domain

import scala.language.{ implicitConversions }

import akka.{ NotUsed }
import akka.util.{ ByteString }
import akka.stream.scaladsl.{ Source }

import shapeless.{ HNil, ::, HList, Generic }

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

  trait Interpretation[T] extends Any {
    final type Self = T

    def mirrorsFrom(self: Self): MirrorRepository
    def storageFrom(self: Self): LockingStorage
  }

  final type Record1[Rest <: HList] = MirrorRepository :: LockingStorage :: Rest

  final implicit def recordI[Rest <: HList]: Interpretation[Record1[Rest]] = new Interpretation[Record1[Rest]] {
    override def mirrorsFrom(self: Self): MirrorRepository = self match {
      case mirrors :: _ ⇒ mirrors
    }
    override def storageFrom(self: Self): LockingStorage = self match {
      case _ :: storage :: _ ⇒ storage
    }
  }

  final case class ADT(
    override val mirrors: MirrorRepository,
    override val storage: LockingStorage
  ) extends FetchManager

  final implicit def genericI[T, Repr](implicit generic: Generic.Aux[T, Repr], i: Interpretation[Repr]) = new Interpretation[T] {
    override def mirrorsFrom(self: Self): MirrorRepository = i mirrorsFrom generic.to(self)
    override def storageFrom(self: Self): LockingStorage = i storageFrom generic.to(self)
  }

  final implicit def apply[T](self: T)(implicit i: Interpretation[T]): FetchManager = new FetchManager {
    override def mirrors: MirrorRepository = i mirrorsFrom self
    override def storage: LockingStorage = i storageFrom self
  }
}
