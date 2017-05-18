package gv.jleon

import shapeless.{ HNil }

import test._

trait MirrorGenerator extends Any {
  this: test.UriGenerator ⇒

  import Mirror._

  implicit def baseUrlGenerator: Arbitrary[BaseUrl] = Arbitrary(
    akkaUriGenerator.arbitrary.map(BaseUrl.apply)
  )

  implicit def prefixGenerator: Arbitrary[Prefix] = Arbitrary(
    arbitrary[String].map(Prefix.apply)
  )

  implicit def mirrorGenerator: Arbitrary[Mirror] = Arbitrary(
    for {
      prefix ← arbitrary[Prefix]
      baseUrl ← arbitrary[BaseUrl]
    }
      yield Mirror(baseUrl :: prefix :: HNil)
  )
}
