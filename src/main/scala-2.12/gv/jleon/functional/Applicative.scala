package gv.jleon.functional

import language.higherKinds

trait Applicative[F[_]] extends Any with Functor[F] {
  def apply[A, B]: F[A ⇒ B] ⇒ F[A] ⇒ F[B]
}
