package gv.jleon2
package model
package facade

trait ErrorHandling {
  this: slice.Mirror ⇒

  type MirrorError <: error.Mirror {
    type Mirror = ErrorHandling.this.Mirror
  }

  val Mirror: MirrorError
}
