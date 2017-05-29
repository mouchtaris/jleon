package gv.jleon2
package model
package error

import language.{ higherKinds }

import scalaz.{ Monad }
import scalaz.syntax.bind._

object Mirror {

  trait Handler extends Any {
    type Result <: mirror.Handler

    def apply[F[_]: Monad](result: F[Result]): F[Result]
  }

}
