package gv.jleon
package functional
package monads

import language.higherKinds
import collection.{ GenTraversable, mutable }
import collection.generic.{ GenericCompanion, CanBuildFrom }

trait Collections {
  import gv.jleon.functional.{ MonadOps ⇒ _ }

  final implicit def canBuildFromWithCompanion[S[T] <: GenTraversable[T], A, B](implicit companion: GenericCompanion[S]): CanBuildFrom[GenTraversable[A], B, S[B]] =
    new CanBuildFrom[GenTraversable[A], B, S[B]] {
      override def apply(from: GenTraversable[A]): mutable.Builder[B, S[B]] = companion.newBuilder
      override def apply(): mutable.Builder[B, S[B]] = companion.newBuilder
    }

  final implicit def traversableMonad[S[T] <: GenTraversable[T]](implicit companion: GenericCompanion[S]): Monad[S] =
    new Monad[S] {
      override def flatMap[A, B]: (A ⇒ S[B]) ⇒ S[A] ⇒ S[B] = f ⇒ _ flatMap f
      override def point[A]: A ⇒ S[A] = companion(_)
    }
}
