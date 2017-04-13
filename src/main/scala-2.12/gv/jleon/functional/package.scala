package gv.jleon

import language.higherKinds

package object functional extends AnyRef
  with Rapply
  with MapReduce
  with typeclasses.Numeric
  with typeclasses.Collections
  with monads.Collections
  with monads.ScalaGenericCompanions
{

  final implicit class MonadOps[F[_], A](val fa: F[A]) extends AnyVal {
    def flatMap[B](f: A ⇒ F[B])(implicit monad: Monad[F]): F[B] = monad flatMap f apply fa
  }

  final implicit class FunctoOps[F[_], A](val fa: F[A]) extends AnyVal {
    def fmap[B](f: A ⇒ B)(implicit functor: Functor[F]): F[B] = functor fmap f apply fa
    def map[B](f: A ⇒ B)(implicit functor: Functor[F]): F[B] = fmap(f)
  }

  final implicit class TraversableOps[F[_], A](val fa: F[A]) extends AnyVal {
    def fold[Z](zero: Z)(op: (Z, A) ⇒ Z)(implicit t: Traversable[F]): Z =
      t.traverse(zero)(op)(fa)
  }

  final implicit class MapReduceOps[F[_], A](val fa: F[A]) extends AnyVal with MapReduce

}
