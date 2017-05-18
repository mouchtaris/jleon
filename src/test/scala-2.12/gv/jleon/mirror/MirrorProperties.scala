package gv.jleon
package mirror

import test._
import Prop._

object MirrorProperties extends Properties("Mirror") {

  import Mirror.{ BaseUrl, Prefix }

  implicit def prefixGenerator: Gen[Prefix] =
    for (s ← arbitrary[String])
      yield s

  implicit def baseUrlGenerator: Gen[BaseUrl] =
    for (u ← arbitrary[Uri])
      yield u

  implicit def mirrorGenerator: Gen[Mirror] =
    for {
      prefix ← arbitrary[Prefix]
      baseUrl ← arbitrary[BaseUrl]
    }
      yield Mirror.ADT(baseUrl, prefix)

  property("baseUrl consistency") = forAll { (b: BaseUrl, p: Prefix) ⇒
    (Mirror.ADT(b, p): Mirror).baseUrl ?= b
  }

  property("prefix consistency") = forAll { (b: BaseUrl, p: Prefix) ⇒
    (Mirror.ADT(b, p): Mirror).prefix ?= p
  }
}
