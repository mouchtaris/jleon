package gv
package isi
package functional

import language.higherKinds

trait Traversable[F[_]] extends Any {
  def traverse[A, Z]: Z ⇒ ((Z, A) ⇒ Z) ⇒ F[A] ⇒ Z
}
