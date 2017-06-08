package gv
package jleon4

import java.nio.file.{ FileSystem ⇒ JFileSystem }

import com.typesafe.config.{ Config ⇒ TSConfig }

import shapeless.{ ::, HList }

import Util._

trait ConfigInterpretationsPackage extends Any {
  // format: OFF
  this: Any
    with ConfigPackage
  ⇒
  // format: ON

  // format: OFF
  implicit def recordConfig[
    config: CouldBe[TSConfig]#t,
    fileSystem: CouldBe[JFileSystem]#t,
    rec <: HList
  ]: // format: ON
  Config[config :: fileSystem :: rec] = self ⇒ new Config.Ops {
    final type Config = TSConfig
    final type FileSystem = JFileSystem
    final val (config: Config) :: (fileSystem: FileSystem) :: _ = self
  }

}
