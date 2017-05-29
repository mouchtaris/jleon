package gv
package isi
package std
package conversions

import language.{ implicitConversions }

import java.{ nio ⇒ jnio, io ⇒ jio }
import jnio.{ ByteBuffer }
import jnio.channels.{ ReadableByteChannel, WritableByteChannel, Channels }
import jio.{ InputStream, OutputStream }

import isi.io.{ ByteSource }
import convertible.{ ~⇒ }

trait JavaIoConversions extends AnyRef {

  final implicit def readableByteChannelToInputStream(channel: ReadableByteChannel): InputStream =
    Channels newInputStream channel

  final implicit val readableByteChannelToInputStreamConversion: ReadableByteChannel ~⇒ InputStream = readableByteChannelToInputStream _

  final implicit def inputStreamToReadableByteChannel(stream: InputStream): ReadableByteChannel =
    Channels newChannel stream

  final implicit val inputStreamToReadableByteChannelConversion: InputStream ~⇒ ReadableByteChannel = inputStreamToReadableByteChannel _

  final implicit def writableByteChannelToOutputStream(channel: WritableByteChannel): OutputStream =
    Channels newOutputStream channel

  final implicit val writableByteChannelToOutputStreamConversion: WritableByteChannel ~⇒ OutputStream = writableByteChannelToOutputStream _

  final implicit def outputStreamToWritableByteChannel(stream: OutputStream): WritableByteChannel =
    Channels newChannel stream

  final implicit val outputStreamToWritableByteChannelConversion: OutputStream ~⇒ WritableByteChannel = outputStreamToWritableByteChannel _

  final implicit def byteSourceToReadableByteChannel[T: ByteSource]: T ~⇒ ReadableByteChannel =
    (self: T) ⇒ new ReadableByteChannel {
      def isOpen: Boolean = true
      def close(): Unit = ()
      def read(into: ByteBuffer): Int = ByteSource[T] readInto (self, into)
    }

}
