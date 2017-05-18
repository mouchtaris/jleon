package gv.jleon.`type`

import scala.language.{ implicitConversions }

trait TypeWrap[T] extends Any {
  def v: T

  @inline final override def toString: String = v.toString
}

object TypeWrap {

  @inline implicit def toValue[T](tw: TypeWrap[T]): T = tw.v

}
