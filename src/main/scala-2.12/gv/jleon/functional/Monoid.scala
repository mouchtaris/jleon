package gv.jleon
package functional

trait Monoid[T] extends Any {
  def zero: T
  def op: (T, T) ⇒ T
}
