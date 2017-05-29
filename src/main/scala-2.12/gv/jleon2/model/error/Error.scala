package gv.jleon2
package model
package error

import language.{ higherKinds, existentials }

import scalaz.{ Monad }

/**
 * Handling Mirror Repository failures
 */
trait Error extends Any
    with slice.Mirror.Types
    with slice.Storage.Types {
  type MirrorHandler <: error.Mirror
  type StorageHandler <: error.Storage

  //noinspection ApparentRefinementOfResultType
  implicit def mirror(implicit mirror: Mirror): MirrorHandler {
    type Result = mirror.Handler
  }
  //noinspection ApparentRefinementOfResultType
  implicit def storage(implicit storage: Storage): StorageHandler {
    type Result = storage.LockResult
  }
}

object Error {

  implicit class WithErrorHandling[F[_], R](val result: F[R]) extends AnyVal {
    @inline
    def withErrorHandling(
      handler: H forSome { type H <: Handler { type Result = R } }
    )(
      implicit
      monad: Monad[F]
    ): F[R] = handler(result)
  }

}
