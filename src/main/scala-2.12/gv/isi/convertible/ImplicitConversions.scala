package gv.isi
package convertible

/**
  * Anything that is implicitly convertible to something, can also be explicitly.
  */
trait ImplicitConversions extends Any {

  @inline
  final implicit def implicitConversion[A, B](implicit a2b: A ⇒ B): A ~⇒ B = a2b

}
