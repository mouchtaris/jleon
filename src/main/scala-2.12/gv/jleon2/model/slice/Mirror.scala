package gv.jleon2
package model
package slice

/**
 * A Mirror Repository Slice
 */
object Mirror {
  trait Types extends Any {
    type Mirror <: mirror.Mirror
  }
}

trait Mirror extends Mirror.Types {
  implicit val Mirror: Mirror
}
