package gv.jleon2
package model.storage

import scala.concurrent.{ Future }

trait Storage {

  type Request

  def tryLock(request: Request): Future[LockResult]

}
