package gv.jleon.functional

trait Monoid[T] extends Any {
  def zero: T
  def op: (T, T) ⇒ T
}
