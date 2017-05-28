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

import leon2._

trait JLeon extends AnyRef
{
  // format: OFF
  this: AnyRef
    with slice.Storage
    with slice.Mirror
  ⇒
  // format: ON

  val ExecutionContexts: facade.ExecutionContexts
  val ErrorHandling: facade.ErrorHandling {
    type Mirror = JLeon.this.Mirror
  }

  def serveRequest(prefix: Mirror.Prefix, request: Storage.Request): Future[Unit] = {
    import ExecutionContexts.RequestProcessing

    object future {
      val mirror = ErrorHandling.Mirror handle Mirror(prefix)
      val lock = Storage tryLock request
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
