package gv.jleon

import shapeless.{ HNil }

import test._
import Prop._
import Mirror._

object MirrorProperties extends Properties("Mirror")
    with MirrorGenerator
    with UriGenerator {
  property("baseUrl consistency") = forAll { (b: BaseUrl, p: Prefix) ⇒
    (b :: p :: true :: HNil).baseUrl ?= b
  }

  property("prefix consistency") = forAll { (b: BaseUrl, p: Prefix) ⇒
    (b :: p :: "Hello" :: HNil).prefix ?= p
  }
}
