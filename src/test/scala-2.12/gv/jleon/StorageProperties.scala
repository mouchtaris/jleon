package gv.jleon

import shapeless.{ HNil }

import test._
import Prop._
import Storage._

object StorageProperties extends Properties("Storage")
    with StorageGenerator
    with JavaNioGenerator
    with UriGenerator {

  property("basePath consistency") =
    forAll { (bp: Storage.Path.t) ⇒
      (bp :: HNil).basePath ?= bp
    }

  property("fetch iff file exists") =
    forAll { (storage: Storage, uri: Uri) ⇒
      storage.fetch(uri).isSuccess ?= File.exists(storage.storagePath(uri))
    }

  property("storagePath is under basePath") =
    forAll { (storage: Storage, uri: Uri) ⇒
      storage.storagePath(uri).toString startsWith storage.basePath.toString
    }

}
