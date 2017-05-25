package gv.jleon

import scala.language.{ implicitConversions }

import shapeless.{ HList, :: }

import `type`.{ TaggedType }

import Fetch._

final case class Fetch(
    fetch: FetchFunc.t
) {
  @inline def apply(uri: Uri): Source[ByteString, NotUsed] = fetch(uri)
}

object Fetch {

  final implicit object FetchFunc extends TaggedType[Uri ⇒ Source[ByteString, NotUsed]]

  trait Interpretation[T] extends Any {
    final type Self = T

    def fetch(self: Self): FetchFunc.t
  }

  final implicit def apply[T: Interpretation](self: T): Fetch = {
    val i: Interpretation[T] = implicitly
    Fetch(
      fetch = i fetch self
    )
  }

  final implicit def recordI[Rest <: HList] = new Interpretation[FetchFunc.t :: Rest] {
    override def fetch(self: Self): FetchFunc.t = self match {
      case f :: _ ⇒ f
    }
  }
}
