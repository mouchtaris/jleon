package gv.jleon2
package model
package slice

object Error {

  trait Types extends AnyRef {
    thisSlice ⇒

    type Error <: error.Error {
      type MirrorHandler <: error.Mirror
      type StorageHandler <: error.Storage
    }

  }
}

trait Error extends Error.Types {
  implicit val Error: Error
}
