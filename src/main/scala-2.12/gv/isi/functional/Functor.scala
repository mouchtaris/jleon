package gv
package isi
package functional

import language.higherKinds

trait Functor[F[_]] extends Any {
  def fmap[A, B]: (A ⇒ B) ⇒ F[A] ⇒ F[B]
}
