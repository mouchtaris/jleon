package gv.jleon2
package model
package slice

trait Error {
  // format: OFF
  this: Any
    with slice.Mirror
    with slice.Storage
  ⇒
  // format: ON

  type Error <: error.Error {
    type Mirror = Error.this.Mirror
    type MirrorHandler = error.Mirror {
      type Result = Error.this.Mirror.Handler
    }

    type Storage = Error.this.Storage
    type StorageHandler = error.Storage {
      type Result = Error.this.Storage.LockResult
    }
  }

  val Error: Error

}
