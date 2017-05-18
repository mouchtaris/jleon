package gv.jleon

import shapeless.{ HNil }

import test._
import Prop._

object MirrorProperties extends Properties("Mirror")
    with MirrorGenerator
    with UriGenerator {

  import Mirror._

  property("baseUrl consistency") = forAll { (b: BaseUrl, p: Prefix) ⇒
    (b :: p :: true :: HNil).baseUrl ?= b
  }

  property("prefix consistency") = forAll { (b: BaseUrl, p: Prefix) ⇒
    (b :: p :: " Hello" :: HNil).prefix ?= p
  }
}
