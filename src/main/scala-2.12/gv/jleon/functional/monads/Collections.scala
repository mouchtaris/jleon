package gv.jleon.functional
package monads

//import language.higherKinds
//import collection.{ GenTraversable }
//import collection.generic.{ CanBuildFrom, GenericCompanion }

trait Collections {
  import gv.jleon.functional.{ MonadOps ⇒ _ }

//  final implicit def collectionMonad[S[T] <: GenTraversable[T], A, B](
//      implicit
//      companion: GenericCompanion[S],
//      cbf: CanBuildFrom[GenTraversable[A], B, S[B]]
//  ): Monad[S] =
//    new Monad[S] {
//
//      override def flatMap[A, B]: (A ⇒ S[B]) ⇒ S[A] ⇒ S[B] =
//        f ⇒ col ⇒ col.flatMap[A, S[B]](f)
//
//      override def point[A]: (A) ⇒ S[A] =
//        a ⇒ companion(a)
//    }

  final implicit case object VectorMonad extends Monad[Vector] {
    override def point[A]: A ⇒ Vector[A] = Vector(_)
    override def flatMap[A, B]: (A ⇒ Vector[B]) ⇒ Vector[A] ⇒ Vector[B] =
      f ⇒ _ flatMap f
  }

  final implicit case object SetMonad extends Monad[Set] {
    override def point[A]: A ⇒ Set[A] = Set(_)
    override def flatMap[A, B]: (A ⇒ Set[B]) ⇒ Set[A] ⇒ Set[B] =
      f ⇒ _ flatMap f
  }
}
