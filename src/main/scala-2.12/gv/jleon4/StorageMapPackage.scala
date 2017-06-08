package gv
package jleon4

import java.nio.file.{ Path ⇒ JPath }

trait StorageMapPackage {
  this: TypeClassPackage ⇒

  import PathInterpretationsPackage._

  trait StorageMap[-T] extends TypeClass.WithTypeParams[T, StorageMap.Ops]

  object StorageMap extends TypeClassCompanion[StorageMap] {

    final val EXT_LOCK = "lock"
    final val EXT_FAILURE = "failed"

    trait Ops extends Any {
      type Base <: JPath
      def base: Base

      final class ForItem(val item: String) {
        val storage: JPath = base resolve item
        val lock: JPath = storage addExt EXT_LOCK
        val failure: JPath = storage addExt EXT_FAILURE
      }

      final def apply(item: String): ForItem = new ForItem(item)
    }

    object Ops
  }

  implicit def storageMapOps[T: StorageMap](self: T): StorageMap.Ops = StorageMap[T](self)
}
