package gv
package jleon2
package model.mirror

import concurrent.{ Future }

import java.nio.{ ByteBuffer }

import akka.stream.scaladsl.{ Source }


trait Handler extends isi.Function with Handler.Types {
  final type FunctionIn = Request
  final type FunctionOut = Source[ByteBuffer, Future[HandlingResult]]
}

object Handler {

  trait Types {
    // Input
    type Request
  }

  trait tp[request] extends Handler {
    final type Request = request
  }

}
