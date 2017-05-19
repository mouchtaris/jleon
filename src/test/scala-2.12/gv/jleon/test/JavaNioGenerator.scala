package gv.jleon
package test

import `type`.{ TaggedType }

object JavaNioGenerator {
  final implicit object PathSegment extends TaggedType[String]
}

trait JavaNioGenerator {
  import JavaNioGenerator._

  private[this] final implicit def pathSegmentGenerator: Arbitrary[PathSegment.t] = Arbitrary(
    Gen.alphaStr.map(PathSegment.apply)
  )

  final implicit def pathGenerator: Arbitrary[Path] = Arbitrary {
    arbitrary[List[PathSegment.t]]
      .map(_.fold("file:///")(_ ++ _))
      .map(Path(_))
  }
}
