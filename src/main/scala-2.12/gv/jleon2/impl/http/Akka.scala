package gv
package jleon2
package impl.http

import util.{ Try, Success, Failure }
import concurrent.{ Future, ExecutionContext }

import java.nio.{ ByteBuffer }

import akka.stream.{ scaladsl ⇒ stream }
import akka.http.{ scaladsl ⇒ http }
import akka.util.{ ByteString }

object Akka {

  object Response {

    final val makeBuffers: ByteString ⇒ () ⇒ Iterator[ByteBuffer] =
      byteString ⇒ () ⇒ byteString.asByteBuffers.iterator

    final val makeSource: ByteString ⇒ stream.Source[ByteBuffer, akka.NotUsed] =
      makeBuffers andThen stream.Source.fromIterator[ByteBuffer]

    final val `ByteString ~> ByteBuffer`: stream.Flow[ByteString, ByteBuffer, akka.NotUsed] =
      stream.Flow[ByteString] flatMapConcat makeSource

    final case class Response(response: http.model.HttpResponse) extends model.http.Response {
      val isSuccess: Try[Unit] = response.status match {
        case http.model.StatusCodes.Success(_) ⇒ Success(())
        case status                            ⇒ Failure(new Exception(status.toString))
      }

      val bytes: stream.Source[ByteBuffer, akka.NotUsed] = response.entity.dataBytes
        .viaMat(`ByteString ~> ByteBuffer`)(stream.Keep.right)
    }

    val modelResponse: http.model.HttpResponse ⇒ model.http.Response = Response
  }

  object Client {
    trait Types extends model.http.Client.Types {
      final type Uri = model.uri.Uri
      final type Response = model.http.Response
    }

    final case class Client()(implicit client: http.HttpExt, mat: akka.stream.Materializer) extends model.http.Client
        with Types {

      import http.client.{ RequestBuilding }

      private[this] implicit val ec: ExecutionContext = mat.executionContext

      val get: Uri ⇒ Future[Response] =
        uri ⇒
          client singleRequest (RequestBuilding Get uri.toAkka) map Response.modelResponse

    }
  }

}
