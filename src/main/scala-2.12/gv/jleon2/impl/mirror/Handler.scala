package gv
package jleon2
package impl.mirror

import concurrent.{ Future }

final case class Handler() extends AnyRef
  with model.mirror.Handler {

  type Request = impl.uri.Types.Uri
  type Result = model.mirror.HandlingResult

  def handle(request: Request): Future[Result] =
    Future failed new NotImplementedError(s"Not handling anything, especially: $request") // TODO: implement
}
