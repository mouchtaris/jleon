package gv.jleon
package functional
package monoid

trait Addition[T] extends Any
  with Monoid[T]

object Addition {
  final implicit def apply[T: typeclass.Addable: typeclass.Zero]: Addition[T] =
    new Addition[T] {
      override def zero: T = implicitly[typeclass.Zero[T]].zero
      override def op: (T, T) ⇒ T = implicitly[typeclass.Addable[T]].op
    }
}
