package gv.jleon2
package model
package slice

trait Error {
  this: slice.Mirror ⇒

  type Error <: error.Error {
    type Mirror = Error.this.Mirror
    type MirrorHandler = error.Mirror.Handler {
      type Result = Error.this.Mirror.Handler
    }
  }

  val Error: Error

}
