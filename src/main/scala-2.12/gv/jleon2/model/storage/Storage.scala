package gv.jleon2
package model
package storage

import concurrent.{ Future }

trait Storage {

  type Request
  type LockResult <: storage.LockResult

  def tryLock(request: Request): Future[LockResult]

}
