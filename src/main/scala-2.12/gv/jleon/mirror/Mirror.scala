package gv.jleon
package mirror

import scala.language.{ implicitConversions }

import Mirror._

trait Mirror extends Any {
  def baseUrl: BaseUrl
  def prefix: Prefix

  def urlFor(path: Uri.Path): Uri = baseUrl withPath (baseUrl.path ++ path)
}

object Mirror {

  final implicit class BaseUrl(val v: Uri) extends AnyVal
  object BaseUrl {
    @inline implicit def toValue(p: BaseUrl): Uri = p.v
  }

  final implicit class Prefix(val v: String) extends AnyVal
  object Prefix {
    @inline implicit def toValue(s: Prefix): String = s.v
  }

  trait Interpretation[T] extends Any {
    def baseUrl(self: T): Uri
    def prefix(self: T): String
  }

  final implicit def apply[T: Interpretation](self: T): Mirror = {
    val i: Interpretation[T] = implicitly
    ADT(
      baseUrl = i.baseUrl(self),
      prefix  = i.prefix(self)
    )
  }

  final case class ADT(
    override val baseUrl: BaseUrl,
    override val prefix:  Prefix
  ) extends Mirror

}
