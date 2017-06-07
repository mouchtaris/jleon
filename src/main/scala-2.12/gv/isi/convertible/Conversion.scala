package gv
package isi
package convertible

import language.{ implicitConversions }

trait Conversion[A, B] {
  def apply(a: A): B
}

object Conversion {
  implicit def toFunction[A, B](conv: Conversion[A, B]): A ⇒ B = conv.apply
}
