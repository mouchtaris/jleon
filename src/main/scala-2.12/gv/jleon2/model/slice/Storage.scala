package gv.jleon2
package model
package slice

/**
 * A Storage slice.
 */
trait Storage {
  type Storage <: storage.Storage
  implicit val Storage: Storage
}
