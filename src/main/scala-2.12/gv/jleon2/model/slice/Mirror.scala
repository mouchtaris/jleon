package gv.jleon2
package model
package slice

/**
 * A Mirror Repository Slice
 */
trait Mirror {
  type Mirror <: mirror.Mirror
  implicit val Mirror: Mirror
}
