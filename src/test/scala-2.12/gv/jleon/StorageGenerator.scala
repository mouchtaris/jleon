package gv.jleon

import shapeless.{ HNil }

import test._

import Storage._

trait StorageGenerator extends Any {
  this: test.JavaNioGenerator ⇒

  final implicit def storagePathGenerator: Arbitrary[Storage.Path.t] = Arbitrary {
    arbitrary[Path].map(Storage.Path.apply)
  }

  final implicit def storageGenerator: Arbitrary[Storage] = Arbitrary {
    for {
      path ← arbitrary[Storage.Path.t]
    } yield Storage(path :: HNil)
  }

}
