package gv.jleon
package mirror

import shapeless.{ HNil }

import test._
import Prop._
import Mirror._

object MirrorProperties extends Properties("Mirror")
    with MirrorGenerator
    with UriGenerator {

  property("baseUrl consistency") =
    forAll { (b: BaseUrl, p: Prefix) ⇒
      (b :: p :: true :: HNil).baseUrl ?= b
    }

  property("prefix consistency") =
    forAll { (b: BaseUrl, p: Prefix) ⇒
      (b :: p :: "Hello" :: HNil).prefix ?= p
    }

  property("urls begin with baseUrl") =
    forAll { (mirror: Mirror, path: Uri.Path) ⇒
      mirror.urlFor(path).toString startsWith mirror.baseUrl.toString
    }
}
