package gv.jleon2
package model

import concurrent.{ Future }
import util.{ Success, Failure }

import scalaz.syntax.monad.{ _ }

import gv.isi
//
import isi.scalaz._

import error.Error.WithErrorHandling

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
    val mirror = Error.mirror
    val storage = Error.storage
  }

//  def serveRequest(prefix: Mirror.Prefix, request: Uri): Future[Unit] = {
//    import ExecutionContexts.RequestProcessing
//    //    import concurrent.ExecutionContext.Implicits.global
//    import mirror.HandlingResult.{ Found ⇒ MirrorFound }
//    import storage.LockResult.{ Acquired ⇒ StorageAcquired, Found ⇒ StorageFound }
//
//    object future {
//      val mirror = {
//        Mirror(prefix)
//          .flatMap { _ handle request }
//          .withErrorHandledBy(error.mirror)
//      }
//      val lock: Future[Storage.LockResult] =
//        Storage tryLock request withErrorHandledBy error.storage
//    }
//
//    future.mirror zip future.lock andThen {
//      case Success((_, StorageFound(rchannel))) ⇒
//        rchannel
//      case Success((MirrorFound(rchannel), StorageAcquired(lockChannel))) ⇒
//        rchannel
//      case Failure(ex) ⇒
//        println {
//          s"""
//             | Failure: $ex
//           """
//        }
//        ex.printStackTrace()
//    } map {
//      _ ⇒ ()
//    }
//    ???
//  }

}
