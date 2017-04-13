package gv.jleon

import language.higherKinds

package object functional extends AnyRef
    with Rapply
    with MapReduce
    with typeclasses.Numeric
    with typeclasses.Collections
    with monads.Collections
    with monads.ScalaGenericCompanions {

  final implicit class MonadDecorations[F[_], A](val fa: F[A]) extends AnyVal with MonadOps[F, A]

  final implicit class FunctorDecorations[F[_], A](val fa: F[A]) extends AnyVal with FunctorOps[F, A]

  final implicit class TraversableDecorations[F[_], A](val fa: F[A]) extends AnyVal with TraversableOps[F, A]

  final implicit class MapReduceOps[F[_], A](val fa: F[A]) extends AnyVal with MapReduce

}
