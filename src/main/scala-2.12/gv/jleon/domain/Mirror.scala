package gv.jleon
package domain

import scala.language.{ implicitConversions }

import shapeless.{ HList, :: }

import gv.jleon.`type`.{ TaggedType }

import Mirror.{ BaseUrl, Prefix }

final case class Mirror(
    baseUrl: BaseUrl,
    prefix:  Prefix,
    name:    String  = ""
) {
  def urlFor(path: Uri.Path): Uri = baseUrl.withPath(baseUrl.path ++ path)
}

object Mirror extends AnyRef
    with MirrorFactory {

  final implicit object BaseUrl extends TaggedType[Uri]
  type BaseUrl = BaseUrl.t

  final implicit object Prefix extends TaggedType[String]
  type Prefix = Prefix.t

  trait Interpretation[T] extends Any {
    type Self = T
    def baseUrl(implicit self: T): BaseUrl
    def prefix(implicit self: T): Prefix
  }

  final implicit def apply[T: Interpretation](self: T): Mirror = {
    val i: Interpretation[T] = implicitly
    implicit val _self = self
    Mirror(
      baseUrl = i.baseUrl,
      prefix  = i.prefix
    )
  }

  final implicit def recordI[Rest <: HList] = new Interpretation[BaseUrl :: Prefix :: Rest] {
    override def baseUrl(implicit self: Self): BaseUrl = self match {
      case u :: _ ⇒ u
    }
    override def prefix(implicit self: Self): Prefix = self match {
      case _ :: p :: _ ⇒ p
    }
  }

}
