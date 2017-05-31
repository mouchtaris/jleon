package gv.jleon2
package model
package slice

object Error {
  trait Types extends AnyRef
      with slice.Mirror.Types
      with slice.Storage.Types {

    type Error <: error.Error {
      type Mirror = Types.this.Mirror
      type MirrorHandler = error.Mirror {
        type Result = Types.this.Mirror.Handler
      }

      type Storage = Types.this.Storage
      type StorageHandler = error.Storage {
        type Result = Types.this.Storage.LockResult
      }
    }

  }
}

trait Error extends Error.Types {
  implicit val Error: Error
}
