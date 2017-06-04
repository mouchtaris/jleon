package gv
package isi
package functional

trait Rapply extends Any {
  final def read[A, B](a: A): (A ⇒ B) ⇒ B = _ apply a
}
