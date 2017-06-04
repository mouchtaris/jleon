package gv
package isi
package functional

import language.higherKinds

trait MonadOps[F[_], A] extends Any {
  this: MonadDecorations[F, A] ⇒

  def flatMap[B](f: A ⇒ F[B])(implicit monad: Monad[F]): F[B] = monad flatMap f apply fa
}
