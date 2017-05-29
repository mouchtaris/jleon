package gv.jleon2
package model
package error

/**
 * Handling Mirror Repository failures
 */
trait Error {
  type Mirror <: model.mirror.Mirror
  type MirrorHandler <: error.Mirror
  type Storage <: model.storage.Storage
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
