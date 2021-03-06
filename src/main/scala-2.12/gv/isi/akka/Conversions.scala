package gv
package isi
package akka

import java.nio.{ ByteBuffer }

import language.{ postfixOps }
import util.{ Try, Success, Failure }
import scala.concurrent.{ Future }

import _root_.akka.stream.scaladsl.{ Source, Flow, Sink, Keep }
import _root_.akka.stream.{ Graph, SourceShape }
import _root_.akka.util.{ ByteString }
import _root_.akka.{ NotUsed }

import io.{ ByteSource, ByteSink }
import convertible._
import std.io._
import std.conversions._
import functional.{ const, lazyconst  }

private[this] object ThisImports extends AnyRef
with functional.Unfold
with CouldBe

import ThisImports._

trait Conversions {

  final implicit def `ByteSource ~⇒ Stream[ByteString]`[BS: ByteSource]: BS ~⇒ Stream[ByteString] =
    source ⇒
      `ByteSource ~⇒ Stream[ByteBuffer]`[BS].apply(source)
        .map(ByteString(_))

  final implicit def `CouldBe[Iterable[T]] ~⇒ Source[T]`[T, S: CouldBe[Iterable[T]]#t]: S ~⇒ Source[T, NotUsed] =
    items ⇒ Source fromIterator (() ⇒ items.iterator)

  final implicit def `Future[Source[T]] ~⇒ Source[T]`[T, M]: Future[Graph[SourceShape[T], M]] ~⇒ Source[T, Future[M]] =
    Source fromFutureSource[T, M] _

  final implicit def `Future[Source[T]] ~⇒ Source[T] (Future Mat)`[T, M]:
    Future[Graph[SourceShape[T], Future[M]]] ~⇒ Source[T, Future[M]] =
    future ⇒
      `Future[Source[T]] ~⇒ Source[T]`(future)
        .mapMaterializedValue(_.flatten)

  final implicit def `Future[T] ~⇒ Source[T]`[T]: Future[T] ~⇒ Source[T, NotUsed] =
    Source fromFuture[T] _


  final implicit def `Try[T] ~⇒ Source[T]`[T]: Try[T] ~⇒ Source[T, NotUsed] = {
    case Success(t) ⇒ Source single t
    case Failure(ex) ⇒ Source failed ex
  }

  final implicit def `Try[Source[T]] ~⇒ Source[T]`[T, M]: Try[Graph[SourceShape[T], M]] ~⇒ Source[T, Try[M]] = {
    case Success(source0) ⇒ Source fromGraph source0 mapMaterializedValue(Success(_))
    case Failure(ex) ⇒ Source failed[T] ex mapMaterializedValue lazyconst(Failure(ex))
  }

  final implicit def `Try[Source[T]] ~⇒ Source[T] (Mat NotUsed)`[T]: Try[Graph[SourceShape[T], NotUsed]] ~⇒ Source[T, NotUsed] =
    _.convertTo[Source[T, Try[NotUsed]]] mapMaterializedValue const(NotUsed)

  final implicit def `ByteSink ~⇒ Sink[ByteBuffer]`[BS: ByteSink]: BS ~⇒ Sink[ByteBuffer, Future[Int]] =
    byteSink ⇒
      Sink
        .fold(0) { (total: Int, buffer: ByteBuffer) ⇒ total + byteSink.writeCompletely(buffer) }

  final implicit val `Flow[ByteString] ⇒ Flow[ByteBuffer]`: Flow[ByteString, ByteBuffer, NotUsed] =
    Flow[ByteString]
      .map { _.asByteBuffers }
      .map { buffers ⇒ () ⇒ buffers.iterator }
      .map { Source fromIterator }
      .flatMapConcat { Predef identity }

  final implicit def `Sink[T] ~⇒ Sink[U]`[T, U, M](
    implicit flow: Flow[U, T, Any]
  ): Sink[T, M] ~⇒ Sink[U, M] =
    sink0 ⇒ flow.toMat(sink0)(Keep.right)

  final implicit def `Source[T] ~⇒ Source[U]`[T, U, M](
    implicit flow: Flow[T, U, Any]
  ): Source[T, M] ~⇒ Source[U, M] =
    source0 ⇒ source0.viaMat(flow)(Keep.left)

}
