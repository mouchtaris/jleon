package gv.jleon2
package model
package slice

/**
  * A Mirror Repository Slice
  */
trait Mirror extends Mirror.Include {
  type Mirror <: mirror.Mirror

  val Mirror: Mirror

  final implicit val MirrorSlice: this.type = this

  trait MirrorSlice extends slice.Mirror {
    type Mirror = Mirror.this.Mirror
    val Mirror = Mirror.this.Mirror
  }
}

object Mirror {
  trait Include {
    implicit val MirrorSlice: slice.Mirror
  }
}
