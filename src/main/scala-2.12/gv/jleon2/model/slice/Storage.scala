package gv.jleon2
package model
package slice

/**
 * A Storage slice.
 */
object Storage {
  trait Types extends Any
      with slice.Uri.Types {
    type Storage <: storage.Storage {
      type Request = Uri
    }
  }
}

trait Storage extends Storage.Types {
  implicit val Storage: Storage
}
