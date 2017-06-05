package gv
package isi

import concurrent.{ Future }

trait RepositoryPackage {

  trait Repository extends Any {
    // Inputs
    type Key

    // Output
    type Value

    def apply(key: Key): Future[Value]
  }

  final object Repository {
    type Aux[k, v] = Repository {
      type Key = k
      type Value = v
    }

    trait tp[k, v] extends Repository {
      final type Key = k
      final type Value = v
    }
  }
}
