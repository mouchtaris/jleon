package gv.jleon
package domain

import scala.language.{ implicitConversions }

import shapeless.{ HNil, ::, HList, Generic }

trait FetchManager extends Any {
  def mirrors: MirrorRepository
  def storage: Storage

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

  def fetch(mirrorIndex: Int, uri: Uri)(implicit prefix: Mirror.Prefix): Source1[ByteString] = {
    val tryFetch = for {
      _ ← storage lock uri
      mirror :: fetch :: HNil ← getMirror(mirrorIndex)
      bytes = fetch(mirror urlFor uri.path)
    } yield bytes

    val bytesSource: Source1[ByteString] = Source
      .fromFuture { Future fromTry tryFetch }
      .flatMapConcat { Predef.identity }
    //    val wat: Sink[ByteString, Future[IOResult]] = storage.write(uri)
    //    val flo = Flow[ByteString]
    //      .flatMapConcat { bytestr ⇒
    //        import
    //        val futio: Future[IOResult] = Source.single(bytestr).runWith(wat)
    //        Source.fromFuture(futio).map {
    //          case result if result.wasSuccessful ⇒ bytestr
    //        }
    //      }

    bytesSource
      //      .via(flo)
      .recoverWithRetries(1, {
        case ex ⇒
          val err = new RuntimeException(s"Fetching $uri failed because of $ex", ex)
          val tryMarkError = storage.markFailed(uri).flatMap(_ ⇒ Failure(err))
          Source fromFuture (Future fromTry tryMarkError)
      })
  }
}

object FetchManager {

  trait Interpretation[T] extends Any {
    final type Self = T

    def mirrorsFrom(self: Self): MirrorRepository
    def storageFrom(self: Self): Storage
  }

  final type Record1[Rest <: HList] = MirrorRepository :: Storage :: Rest

  final implicit def recordI[Rest <: HList]: Interpretation[Record1[Rest]] = new Interpretation[Record1[Rest]] {
    override def mirrorsFrom(self: Self): MirrorRepository = self match {
      case mirrors :: _ ⇒ mirrors
    }
    override def storageFrom(self: Self): Storage = self match {
      case _ :: storage :: _ ⇒ storage
    }
  }

  final case class ADT(
    override val mirrors: MirrorRepository,
    override val storage: Storage
  ) extends FetchManager

  final implicit def genericI[T, Repr](implicit generic: Generic.Aux[T, Repr], i: Interpretation[Repr]) = new Interpretation[T] {
    override def mirrorsFrom(self: Self): MirrorRepository = i mirrorsFrom generic.to(self)
    override def storageFrom(self: Self): Storage = i storageFrom generic.to(self)
  }

  final implicit def apply[T](self: T)(implicit i: Interpretation[T]): FetchManager = new FetchManager {
    override def mirrors: MirrorRepository = i mirrorsFrom self
    override def storage: Storage = i storageFrom self
  }
}
