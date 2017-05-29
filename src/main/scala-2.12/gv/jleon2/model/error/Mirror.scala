package gv.jleon2
package model
package error

import language.{ higherKinds }

import scalaz.{ Monad }
import scalaz.syntax.bind._

object Mirror {

  trait Handler extends Any {
    type Result <: mirror.Handler

    final def apply[F[_]: Monad](result: F[Result]): F[Result] = {
      result flatMap { result ⇒ Monad[F].pure(result) } map {
        result ⇒
          result
      }
    }
  }

}
