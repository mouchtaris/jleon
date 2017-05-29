package gv.jleon2
package model
package slice

/**
 * A Mirror Repository Slice
 */
object Mirror {

  trait Types extends Any
      with slice.Uri {

    type Mirror <: mirror.Mirror {
      type Handler = mirror.Handler {
        type Request = Types.this.Uri
      }
    }

  }

}

trait Mirror extends Mirror.Types {
  implicit val Mirror: Mirror
}
