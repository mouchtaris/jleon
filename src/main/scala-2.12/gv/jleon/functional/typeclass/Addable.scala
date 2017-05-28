package gv.jleon
package functional
package typeclass

trait Addable[T] extends Any {
  def op: (T, T) ⇒ T
}
