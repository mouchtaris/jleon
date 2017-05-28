package gv.jleon
package functional

import language.higherKinds

trait TraversableOps[F[_], A] extends Any {
  this: TraversableDecorations[F, A] ⇒

  def fold[Z](zero: Z)(op: (Z, A) ⇒ Z)(implicit t: Traversable[F]): Z =
    t.traverse(zero)(op)(fa)
}
