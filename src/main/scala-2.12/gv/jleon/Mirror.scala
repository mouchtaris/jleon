package gv.jleon

import scala.language.{ implicitConversions }

import shapeless.{ HList, HNil, :: }

import gv.jleon.`type`.{ TaggedType }

import Mirror._

final case class Mirror(
  baseUrl: BaseUrl,
  prefix: Prefix
) {
  def urlFor(path: Uri.Path): Uri = baseUrl.withPath(baseUrl.path ++ path)
}

object Mirror {

  final implicit object BaseUrl extends TaggedType[Uri]
  type BaseUrl = BaseUrl.t

  final implicit object Prefix extends TaggedType[String]
  type Prefix = Prefix.t

  trait Interpretation[T] extends Any {
    type Self = T
    def baseUrl(self: T): BaseUrl
    def prefix(self: T): Prefix
  }

  final implicit def recordI[Rest <: HList] = new Interpretation[BaseUrl :: Prefix :: Rest] {
    override def baseUrl(self: Self): BaseUrl = self match {
      case u :: _ ⇒ u
    }
    override def prefix(self: Self): Prefix = self match {
      case _ :: p :: _ ⇒ p
    }
  }

  final implicit def apply[T: Interpretation](self: T): Mirror = {
    val i: Interpretation[T] = implicitly
    Mirror(
      baseUrl = i baseUrl self,
      prefix = i prefix self
    )
  }

}
