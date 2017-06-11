package gv
package isi
package akka

import java.nio.channels.{ ReadableByteChannel }

import _root_.akka.stream.scaladsl.{ Source }
import _root_.akka.util.{ ByteString }
import _root_.akka.{ NotUsed }

import isi.convertible._

trait Conversions {

  final implicit val `ReadableByteChannel ~⇒ Source[ByteString]`:

}
