package gv
package jleon2
package model.mirror

import concurrent.{ Future }

trait Handler extends isi.Function with Handler.Types {
  final type FunctionIn = Request
  final type FunctionOut = Future[Result]
}

object Handler {

  trait Types {
    // Input
    type Request

    // Output
    type Result <: HandlingResult
  }

  trait tp[request, result <: HandlingResult] extends Handler {
    final type Request = request
    final type Result = result
  }

}
