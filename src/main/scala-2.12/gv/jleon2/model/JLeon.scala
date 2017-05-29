package gv.jleon2
package model

import concurrent.{ Future }
import util.{ Success, Failure }

import scalaz.syntax.monad.{ _ }

import gv.isi
//
import isi.scalaz._

trait JLeon extends AnyRef {
  // format: OFF
  this: AnyRef
    with slice.Storage
    with slice.Mirror
    with slice.Error
  ⇒
  // format: ON

  val ExecutionContexts: facade.ExecutionContexts

  object error {
    val mirror: Error.MirrorHandler = Error.mirror
    val storage: Error.StorageHandler = Error.storage
  }

  def serveRequest(prefix: Mirror.Prefix, request: Storage.Request): Future[Unit] = {
    import ExecutionContexts.RequestProcessing

    object future {
      val mirror: Future[Mirror.Handler] = error.mirror(Mirror(prefix))
      val lock: Future[storage.LockResult] = error.storage(Storage tryLock request)
    }

    future.mirror tuple future.lock andThen {
      case Success((mirror, lock)) ⇒
        println {
          s"""
             | mirror: $mirror
             | lock: $lock
          """
        }
      case Failure(ex) ⇒
        println {
          s"""
             | Failure: $ex
           """
        }
        ex.printStackTrace()
    } map {
      _ ⇒ ()
    }
  }

}
