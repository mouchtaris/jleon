package gv.jleon2
package model

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.{ Try, Success, Failure }

import shapeless.{ HNil, :: }

import gv.{ jleon ⇒ leon }
import gv.{ jleon2 ⇒ leon2 }
import gv.isi
//
import isi.std.conversions._
import isi.convertible._

trait JLeon {

  import leon2._

  type Storage <: storage.Storage
  val Storage: Storage
  final type StorageRequest = Storage.Request

  type MirrorRepository <: mirror.MirrorRepository
  val MirrorRepository: MirrorRepository
  final type MirrorPrefix = MirrorRepository.Prefix
  final type Mirror = MirrorRepository.Mirror

  val ExecutionContexts: facade.ExecutionContexts

  def serveRequest(prefix: MirrorPrefix, request: StorageRequest): Future[Unit] = {
    import ExecutionContexts.RequestProcessing

    object future {
      val mirror: Future[Mirror] = MirrorRepository apply prefix
      val lock: Future[storage.LockResult] = Storage tryLock request
    }

    future.mirror
      .flatMap { mirror ⇒ future.lock.map { lock ⇒ mirror :: lock :: HNil } }
      .andThen {
        case Success(mirror :: lock :: HNil) ⇒
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
      }
      .map(_ ⇒ ())
  }

}
