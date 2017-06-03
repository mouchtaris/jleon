package gv.jleon2
package model
package slice

/**
 * A Mirror Repository Slice
 */
object Mirror {

  trait Types extends Any
      with slice.Uri.Types {

    type Factory <: mirror.RepositoryFactory

    type Mirror <: Factory#Repository {
      // Outputs
      type Handler <: mirror.Handler {
        // Inputs
        type Request >: Types.this.Uri
      }
    }

  }

}

trait Mirror extends Mirror.Types {
  implicit val Mirror: Mirror
}
