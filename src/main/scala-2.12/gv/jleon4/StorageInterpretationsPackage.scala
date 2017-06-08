package gv
package jleon4

trait StorageInterpretationsPackage {
  // format: OFF
  this: Any
    with StorageMapPackage
    with StoragePackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordStorage[
    storageMap: CouldBe[StorageMap.Ops]#t,
    rec <: HList
  ]: // format: ON
  Storage[storageMap :: rec] = self ⇒ new Storage.Ops {
    final type StorageMap = StorageMap.Ops
    final val storageMapSource :: _ = self
    final val storageMap: StorageMap = storageMapSource
  }

}
