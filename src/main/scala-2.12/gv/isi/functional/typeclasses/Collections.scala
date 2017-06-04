package gv
package isi
package functional
package typeclasses

import language.higherKinds
import collection.generic.{ CanBuild, CanBuildFrom }

trait Collections {

  final implicit def zeroCollection[S[_], A](implicit cbf: CanBuild[Nothing, S[A]]): typeclass.Zero[S[A]] =
    new typeclass.Zero[S[A]] {
      override def zero: S[A] = cbf().result()
    }

  final implicit def addableCollection[S[T] <: TraversableOnce[T], A](implicit cbf: CanBuildFrom[S[A], A, S[A]]): typeclass.Addable[S[A]] =
    new typeclass.Addable[S[A]] {
      override def op: (S[A], S[A]) ⇒ S[A] = {
        case (a, b) ⇒ (cbf() ++= a ++= b).result()
      }
    }

}
