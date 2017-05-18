package gv.jleon
package crypto

import test._

trait DigestionGenerators {

  final implicit def digestionGenerator: Arbitrary[Digestion] = Arbitrary(
    Gen.const(Digestion.SHA512)
  )

}
