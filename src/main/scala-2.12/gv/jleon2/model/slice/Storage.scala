package gv.jleon2
package model
package slice

/**
 * A Storage slice.
 */
object Storage {
  trait Types extends Any {
    type Storage <: storage.Storage
  }
}

trait Storage extends Storage.Types {
  implicit val Storage: Storage
}
