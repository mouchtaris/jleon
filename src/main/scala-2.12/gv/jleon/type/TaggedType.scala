package gv.jleon.`type`

import scala.language.{ implicitConversions }

import shapeless.tag

trait TaggedType[_U] {
  final type U = _U

  sealed trait Tag
  final type t = tag.@@[U, Tag]

  final implicit def apply(u: U): t = tag[Tag].apply[U](u)
}
