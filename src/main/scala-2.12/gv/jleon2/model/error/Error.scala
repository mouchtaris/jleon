package gv.jleon2
package model
package error

import language.{ higherKinds }

import scalaz.{ Monad }

/**
 * Handling Mirror Repository failures
 */
trait Error extends Any
    with slice.Mirror.Types
    with slice.Storage.Types {
  type MirrorHandler <: error.Mirror
  type StorageHandler <: error.Storage

  implicit def mirror: MirrorHandler
  implicit def storage: StorageHandler
}

object Error {

  implicit class WithErrorHandling[F[_], R](val result: F[R]) extends AnyVal {
    @inline
    def withErrorHandledBy(handler: Handler)(implicit m: Monad[F]): F[R] = handler(result)
  }

}
