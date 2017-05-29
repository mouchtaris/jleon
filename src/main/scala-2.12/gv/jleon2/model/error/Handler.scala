package gv.jleon2
package model
package error

import language.{ higherKinds }
import scalaz.{ Monad }

trait Handler extends Any {
  def apply[F[_]: Monad, Result](result: F[Result]): F[Result]
}
