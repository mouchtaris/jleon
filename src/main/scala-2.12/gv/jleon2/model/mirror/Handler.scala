package gv.jleon2
package model
package mirror

import concurrent.{ Future }

trait Handler {

  type Request
  type Result <: HandlingResult

  def handle(request: Request): Future[Result]
}
