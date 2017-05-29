package gv.jleon2
package model
package error

import language.{ higherKinds, existentials }
import concurrent.{ Future }

import shapeless.{ HNil, :: }
import scalaz.{ Bind }

/**
 * Handling Mirror Repository failures
 */
trait Error {
  type Mirror <: model.mirror.Mirror
  type MirrorHandler <: error.Mirror.Handler

  def mirror(implicit mirror: Mirror): MirrorHandler
}

