package gv
package jleon2
package model.mirror

import java.nio.{ ByteBuffer }

import language.{ implicitConversions }
import concurrent.{ Future }

import akka.stream.scaladsl.{ Source }

trait HttpHandler extends Handler with HttpHandler.Slices {

  protected[this] val ExecutionContext: HttpHandler.ExecutionContext

  private[this] implicit val _ec = ExecutionContext.ec

  private[this] val makeResult: HttpResponse ⇒ Future[HandlingResult] =
    ((_: HttpResponse).isSuccess) andThen (_ map (_ ⇒ HandlingResult.Found())) andThen Future.fromTry

  def apply(request: Request): Source[ByteBuffer, Future[HandlingResult]] = {
    val futureResponse: Future[HttpResponse] = HttpClient get request
    val futureResult: Future[HandlingResult] = futureResponse flatMap makeResult
    val futureByteSource: Future[Source[ByteBuffer, akka.NotUsed]] = futureResponse map (_.bytes)
    Source fromFutureSource futureByteSource mapMaterializedValue (_ flatMap (_ ⇒ futureResult))
  }

}

object HttpHandler {
  trait Types extends Any {
    // Inputs
    type Client <: model.http.Client
  }

  trait Slices extends Types {
    val HttpClient: Client

    // Overriding Handler Request
    type Request <: HttpClient.Uri

    // Outputs
    final type HttpResponse = HttpClient.Response
  }

  class ExecutionContext(val ec: concurrent.ExecutionContext) extends AnyVal
}
