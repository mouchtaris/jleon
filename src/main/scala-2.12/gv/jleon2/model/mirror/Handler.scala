package gv.jleon2
package model
package mirror

import concurrent.{ Future }

trait Handler {

  // Inputs
  type Request

  // Outputs
  type Result <: HandlingResult

  def handle(request: Request): Future[Result]
}
