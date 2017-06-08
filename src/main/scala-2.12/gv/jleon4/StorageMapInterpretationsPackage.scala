package gv
package jleon4

import java.nio.file.{ Path ⇒ JPath }
import shapeless.{ ::, HList }

import Util._

trait StorageMapInterpretationsPackage extends Any {
  // format: OFF
  this: Any
    with Util
    with StorageMapPackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordStorageMap[
    base: CouldBe[JPath]#t,
    rec <: HList
  ]: // format: ON
  StorageMap[base :: rec] = self ⇒ new StorageMap.Ops {
    final type Base = JPath
    final val baseSource :: _ = self
    final val base: Base = baseSource
  }
}
