package gv.jleon2
package model
package error

import concurrent.{ Future }

import shapeless.{ HNil, :: }

/**
  * Handling Mirror Repository failures
  */
trait Mirror {
  type Mirror <: mirror.Mirror

  final type Prefix = Mirror # Prefix
  final type Handler = Mirror # Handler
  final type Result = Future[Prefix :: Handler :: HNil]

  final def handle(futureMirror: Result): Result = ??? // TODO implement

}
