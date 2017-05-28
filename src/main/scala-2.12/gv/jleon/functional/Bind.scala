package gv.jleon
package functional

import language.higherKinds

trait Bind[F[_]] extends Any {
  def point[A]: A ⇒ F[A]
}
