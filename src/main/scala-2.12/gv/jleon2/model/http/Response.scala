package gv
package jleon2
package model.http

import java.nio.{ ByteBuffer }

import akka.{ NotUsed }
import akka.stream.scaladsl.{ Source }

trait Response {

  /**
    * @return Success(()) for ok, Failure() with explanation otherwise
    */
  val isSuccess: util.Try[Unit]

  val bytes: Source[ByteBuffer, NotUsed]

}

