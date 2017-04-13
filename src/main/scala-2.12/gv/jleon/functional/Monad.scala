package gv.jleon.functional

import language.higherKinds

trait Monad[F[_]] extends Any with Applicative[F] with Bind[F] {
  def flatMap[A, B]: (A ⇒ F[B]) ⇒ F[A] ⇒ F[B]

  final def pointf[A, B]: (A ⇒ B) ⇒ A ⇒ F[B] = _ andThen point

  final override def fmap[A, B]: (A ⇒ B) ⇒ F[A] ⇒ F[B] =
    point andThen apply

  final override def apply[A, B]: F[A ⇒ B] ⇒ F[A] ⇒ F[B] =
    ff ⇒ fa ⇒ {
      val pointmap: (A ⇒ B) ⇒ F[B] = pointf andThen flatMap[A, B] andThen read(fa)
      flatMap(pointmap)(ff)
    }
}
