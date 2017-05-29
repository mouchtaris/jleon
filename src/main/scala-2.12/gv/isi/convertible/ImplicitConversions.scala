package gv
package isi
package convertible

import scala.language.{ implicitConversions }

/**
 * Anything for which there exists an explicit conversion,
 * has an implicit one as well.
 */
trait ImplicitConversions extends Any {

  @inline
  final implicit def implicitConversionFromConversion[A, B](a: A)(implicit conv: A ~⇒ B): B = conv(a)

}
