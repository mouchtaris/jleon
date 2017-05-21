package gv.jleon
package domain

import shapeless.{ HNil }

object StorageFactory {

  private[this] object key {
    val BasePath: String = "basePath"
  }

  implicit class StorageConfig(val self: config.tsConfig) extends AnyVal {

    def basePath: Path = Path(self getString key.BasePath)

  }

}

import StorageFactory._

trait StorageFactory extends Any {

  def fromConfig(
    implicit
    config: StorageConfig
  ): Storage = {
    Storage.Path(config.basePath) :: HNil
  }

}
