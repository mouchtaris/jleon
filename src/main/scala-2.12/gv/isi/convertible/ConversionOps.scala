package gv.isi
package convertible

object ConversionOps {

  trait Ops[A, B] extends Any {
    @inline
    def self: A ⇒ B

    @inline
    final def apply(a: A): B = self(a)
  }

}
