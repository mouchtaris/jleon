package gv.jleon
package storage

import shapeless.{ HNil }

object StorageFactory {

  private[this] object key {
    val BasePath: String = "basePath"
  }

  implicit class StorageConfig(val self: config.tsConfig) extends AnyVal {

    def basePath: Path = Path(self getString key.BasePath)

  }

}

trait StorageFactory extends Any {

  def fromConfig(
    implicit
    config: StorageConfig
  ): Storage = {
    Storage.Path(config.basePath) :: HNil
  }

}
