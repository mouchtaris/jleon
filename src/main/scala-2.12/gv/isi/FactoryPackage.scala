package gv
package isi

import util.{ Try }

private[isi] trait FactoryPackage {

  trait Factory extends Any with Function {
    type Source
    type Construct

    final type FunctionIn = Source
    final type FunctionOut = Try[Construct]
  }

  final object Factory {
    type Aux[s, c] = Factory {
      type Source = s
      type Construct = c
    }

    trait tp[s, c] extends Factory {
      final type Source = s
      final type Construct = c
    }
  }
}
