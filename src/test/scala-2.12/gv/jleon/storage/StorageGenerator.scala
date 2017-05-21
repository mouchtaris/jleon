package gv.jleon
package storage

import shapeless.{ HNil }

import test._

trait StorageGenerator extends Any {
  this: test.JavaNioGenerator ⇒

  final implicit def storagePathGenerator: Arbitrary[Storage.Path] = Arbitrary {
    arbitrary[Path].map(Storage.Path.apply)
  }

  final implicit def storageGenerator: Arbitrary[Storage] = Arbitrary {
    for {
      path ← arbitrary[Storage.Path]
    } yield Storage(path :: HNil)
  }

}
