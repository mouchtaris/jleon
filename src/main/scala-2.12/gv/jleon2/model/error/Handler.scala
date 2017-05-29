package gv.jleon2
package model
package error

import language.{ higherKinds }
import scalaz.{ Monad }

trait Handler extends Any {
  type Result
  def apply[F[_]: Monad](result: F[Result]): F[Result]
}
