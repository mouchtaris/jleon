package gv
package jleon4

trait StorageFactoryInterpretationsPackage extends Any {
  // format: OFF
  this: Any
    with ConfigPackage
    with StorageFactoryPackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordStorageFactory[
    config: CouldBe[Config.Ops]#t,
    rec <: HList
  ] // format: ON
  : StorageFactory[config :: rec] = self ⇒ new StorageFactory.Ops {
    final type Config = StorageFactoryInterpretationsPackage.this.Config.Ops
    final val configSource :: _ = self
    final val config: Config = configSource
  }

}
