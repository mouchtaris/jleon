package gv
package isi
package functional

import language.higherKinds

trait MapReduce extends Any {
  final def mapReduce[A, A1 >: A, F[_]](implicit t: Traversable[F], m: Monoid[A1]): F[A1] ⇒ A1 =
    t.traverse(m.zero)(m.op)
}
