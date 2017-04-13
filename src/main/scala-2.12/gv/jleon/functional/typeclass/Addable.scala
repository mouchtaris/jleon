package gv.jleon.functional
package typeclass

trait Addable[T] extends Any {
  def op: (T, T) ⇒ T
}
