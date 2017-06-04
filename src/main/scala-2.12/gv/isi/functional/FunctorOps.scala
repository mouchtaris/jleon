package gv
package isi
package functional

import language.higherKinds

trait FunctorOps[F[_], A] extends Any {
  this: FunctorDecorations[F, A] ⇒

  def fmap[B](f: A ⇒ B)(implicit functor: Functor[F]): F[B] = functor fmap f apply fa
  def map[B](f: A ⇒ B)(implicit functor: Functor[F]): F[B] = fmap(f)
}
