package gv
package isi
package functional

trait BindersPackage {

  @inline final def pfconst[T](t: T): Any ~~> T = { case _ ⇒ t }

  @inline final def const[T](t: T): Any ⇒ T = _ ⇒ t

  @inline final def lazypf[T](t: ⇒ T): Any ~~> T = { case _ ⇒ t }

  @inline final def lazyconst[T](t: ⇒ T): Any ⇒ T = _ ⇒ t
}
