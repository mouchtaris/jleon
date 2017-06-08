package gv
package jleon4

import language.{ implicitConversions }

import shapeless.{ HNil }

import isi.std.conversions._

trait StorageFactoryPackage {
  // format: OFF
  this: Any
    with ConfigPackage
    with Util
    with TypeClassPackage
    with StorageMapPackage
    with StorageMapInterpretationsPackage
    with StoragePackage
    with StorageInterpretationsPackage
  ⇒
  // format: ON

  trait StorageFactory[-T] extends Any with TypeClass.WithTypeParams[T, StorageFactory.Ops]

  //noinspection TypeAnnotation
  object StorageFactory extends TypeClassCompanion[StorageFactory] {

    trait Ops extends Any {
      type Config <: StorageFactoryPackage.this.Config.Ops

      def config: Config

      final def storageMap = couldBe[StorageMap.Ops] {
        (config getUri "basePath") :: HNil
      }

      final def storage = couldBe[Storage.Ops] {
        storageMap :: HNil
      }
    }

    object Ops

  }

  implicit def storageFactoryOps[T: StorageFactory](self: T): StorageFactory.Ops = StorageFactory[T](self)
}
