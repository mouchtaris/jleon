package gv
package jleon4

trait Util extends AnyRef
    with isi.CouldBe {
  final def pf[T](t: ⇒ T): Any ~~> T = { case _ ⇒ t }

  /** syntax-require: block is never run */
  final def squire(t: ⇒ Any): Unit = ()
}

object Util extends Util
