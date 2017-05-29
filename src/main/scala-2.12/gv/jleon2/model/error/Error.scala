package gv.jleon2
package model
package error

import language.{ higherKinds, existentials }
import concurrent.{ Future }

import shapeless.{ HNil, :: }
import scalaz.{ Bind }

object Error {
  final implicit class MirrorHandler[Mirror <: model.mirror.Mirror](val mirror: Mirror)
    extends AnyVal
    with Mirror.Handler
    {
//      type Result = mirror.Handler
  }
}
/**
  * Handling Mirror Repository failures
  */
trait Error {
  type Mirror <: model.mirror.Mirror

  final def mirror(implicit mirror: Mirror): Error.MirrorHandler[mirror.type] = new Error.MirrorHandler[mirror.type](mirror)
}

