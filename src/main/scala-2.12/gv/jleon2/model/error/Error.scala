package gv.jleon2
package model
package error

/**
 * Handling Mirror Repository failures
 */
trait Error extends Any
    with slice.Mirror.Types
    with slice.Storage.Types {
  type MirrorHandler <: error.Mirror
  type StorageHandler <: error.Storage

  //noinspection ApparentRefinementOfResultType
  def mirror(implicit mirror: Mirror): MirrorHandler {
    type Result = mirror.Handler
  }
  //noinspection ApparentRefinementOfResultType
  def storage(implicit storage: Storage): StorageHandler {
    type Result = storage.LockResult
  }
}
