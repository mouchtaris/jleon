package gv.jleon
package functional
package typeclasses

trait Numeric {

  final implicit case object ZeroLong extends typeclass.Zero[Long] {
    def zero: Long = 0
  }

  final implicit case object AddableLong extends typeclass.Addable[Long] {
    def op: (Long, Long) ⇒ Long = _ + _
  }

}
