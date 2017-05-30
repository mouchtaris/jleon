package gv
package isi
package std
package conversions

import language.{ postfixOps }

import java.{ nio ⇒ jnio, io ⇒ jio }
import jnio.{ ByteBuffer }
import jnio.channels.{ ReadableByteChannel, WritableByteChannel, Channels }
import jio.{ InputStream, OutputStream }

import isi.io.{ ByteSource }
import convertible.{ ~⇒ }

trait JavaIoConversions extends AnyRef {

  final implicit val readableByteChannelToInputStreamConversion: ReadableByteChannel ~⇒ InputStream =
    Channels newInputStream

  final implicit val inputStreamToReadableByteChannelConversion: InputStream ~⇒ ReadableByteChannel =
    Channels newChannel

  final implicit val writableByteChannelToOutputStreamConversion: WritableByteChannel ~⇒ OutputStream =
    Channels newOutputStream

  final implicit val outputStreamToWritableByteChannelConversion: OutputStream ~⇒ WritableByteChannel =
    Channels newChannel _

  final implicit def byteSourceToReadableByteChannel[T: ByteSource]: T ~⇒ ReadableByteChannel =
    (self: T) ⇒ new ReadableByteChannel {
      def isOpen: Boolean = true
      def close(): Unit = ()
      def read(into: ByteBuffer): Int = ByteSource[T] readInto (self, into)
    }

}

object JavaIoConversions extends JavaIoConversions
