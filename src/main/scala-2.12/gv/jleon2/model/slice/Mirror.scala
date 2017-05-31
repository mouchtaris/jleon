package gv.jleon2
package model
package slice

/**
 * A Mirror Repository Slice
 */
object Mirror {

  trait Types extends Any
      with slice.Uri.Types {

    type Mirror <: mirror.Mirror {
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
