package gv.jleon
package crypto

import test._
import Prop._
import Digestion._

object DigestionProperties extends Properties("Digestion")
    with DigestionGenerators {
  property("consistency") = forAll { (d: Digestion, bytes: Bytes) ⇒
    d.digest(bytes) sameElements d.digest(bytes)
  }
}
