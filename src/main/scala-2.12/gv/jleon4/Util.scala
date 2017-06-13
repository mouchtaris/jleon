package gv
package jleon4

trait Util extends AnyRef
    with isi.CouldBe {
  final def pf[T](t: ⇒ T): Any ~~> T = { case _ ⇒ t }
}

object Util extends Util
