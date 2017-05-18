package gv

import java.{ io ⇒ jio }
import java.nio.{ file ⇒ jfile }
import java.nio.{ channels ⇒ jchannels }

import scala.{ util ⇒ sutil }
import scala.{ concurrent ⇒ sconcurrent }

import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }

package object jleon {
  protected[jleon]type IOException = jio.IOException

  protected[jleon]type ReadableByteChannel = jchannels.ReadableByteChannel

  protected[jleon]type Path = jfile.Path

  protected[jleon] final def nonFatalCatch[T]: sutil.control.Exception.Catch[T] = sutil.control.Exception.nonFatalCatch

  protected[jleon]type Try[T] = sutil.Try[T]
  protected[jleon] val Try = sutil.Try
  protected[jleon]type Success[T] = sutil.Success[T]
  protected[jleon] val Success = sutil.Success
  protected[jleon]type Failure[T] = sutil.Failure[T]
  protected[jleon] val Failure = sutil.Failure

  protected[jleon]type Future[T] = sconcurrent.Future[T]
  protected[jleon] val Future = sconcurrent.Future
  protected[jleon]type ExecutionContext = sconcurrent.ExecutionContext

  protected[jleon]type Uri = AkkaUri
  protected[jleon] val Uri = AkkaUri
}
