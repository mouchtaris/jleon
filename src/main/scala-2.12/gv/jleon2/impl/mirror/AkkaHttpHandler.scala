package gv
package jleon2
package impl.mirror

import concurrent.{ Future }
import util.{ Success, Failure }

import akka.http.{ scaladsl ⇒ http }
import akka.stream.{ scaladsl ⇒ stream }

import http.model.{ Uri, HttpRequest }

import model.mirror.{ HandlingResult }

final case class AkkaHttpHandler(
  implicit
  akkaHttp:     http.HttpExt,
  materializer: akka.stream.Materializer,
  ec:           AkkaHttpHandler.ExecutionContext
) extends model.mirror.Handler
    with AkkaHttpHandler.Types {

  import http.client.RequestBuilding

  @inline
  private[this] def request(implicit uri: Uri): HttpRequest = RequestBuilding Get uri

  def apply(uri: Uri): Future[Result] = {
    implicit val _uri = uri
    implicit val _ec = ec.ec

    val f1: Future[http.model.HttpResponse] = akkaHttp.singleRequest(request)
    val f2: Future[akka.util.ByteString] = f1 flatMap { response ⇒
      response.entity.dataBytes runWith stream.Sink.head
    }
    val f3: Future[java.nio.channels.ReadableByteChannel] = f2 map { bytestring ⇒
      new java.nio.channels.ReadableByteChannel {
        /**
         * As seen from <$anon: java.nio.channels.ReadableByteChannel>, the missing signatures are as follows.
         *  For convenience, these are usable as stub implementations.
         */
        // Members declared in java.nio.channels.Channel
        def close(): Unit = ()

        def isOpen(): Boolean = true

        // Members declared in java.nio.channels.ReadableByteChannel
        def read(src: java.nio.ByteBuffer): Int = {
          
          ???
        }
      }
    }
    val f4: Future[Result] = f3 map { model.mirror.HandlingResult.Found }

    f4
  }

}

object AkkaHttpHandler {

  trait Types extends model.mirror.Handler.Types {
    final type Request = Uri
    final type Result = HandlingResult
  }

  final implicit class ExecutionContext(val ec: concurrent.ExecutionContext) extends AnyVal
}
