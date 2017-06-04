package gv
package isi
package functional

trait Monoid[T] extends Any {
  def zero: T
  def op: (T, T) ⇒ T
}
