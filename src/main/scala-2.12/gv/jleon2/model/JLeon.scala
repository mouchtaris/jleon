package gv.jleon2
package model

import language.{ existentials }
import concurrent.{ Future, ExecutionContext }
import util.{ Try, Success, Failure }

import shapeless.{ HNil, :: }
import scalaz.std._
import scalaz.syntax._
import scalaz.Monad
import Monad._
import scalaz.syntax.monad._

import gv.{ jleon ⇒ leon }
import gv.{ jleon2 ⇒ leon2 }
import gv.isi
//
import isi.std.conversions._
import isi.convertible._
import isi.scalaz._

import leon2._

trait JLeon extends AnyRef
{
  // format: OFF
  this: AnyRef
    with slice.Storage
    with slice.Mirror
    with slice.Error
  ⇒
  // format: ON

  val ExecutionContexts: facade.ExecutionContexts

  def serveRequest(prefix: Mirror.Prefix, request: Storage.Request): Future[Unit] = {
    import ExecutionContexts.RequestProcessing

    object future {
      val mirror = Error.mirror.apply(Mirror(prefix))
      val lock = Storage tryLock request
    }

    future.mirror
      .flatMap { mirror ⇒ future.lock.map { lock ⇒ mirror :: lock :: HNil } }
      .flatMap { case mirror :: lock :: HNil ⇒ Future successful mirror.stigma }
//      .andThen {
//        case Success(mirror :: lock :: HNil) ⇒
//          println {
//            s"""
//               | mirror: $mirror
//               | lock: $lock
//            """
//          }
//        case Failure(ex) ⇒
//          println {
//            s"""
//               | Failure: $ex
//             """
//          }
//          ex.printStackTrace()
//      }
//      .map(_ ⇒ ())
    ???
  }

}
