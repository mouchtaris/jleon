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

  final implicit val `ReadableByteChannel ~=> InputStream`: ReadableByteChannel ~⇒ InputStream =
    Channels newInputStream

  final implicit val `InputStream ~=> ReadableByteChannel`: InputStream ~⇒ ReadableByteChannel =
    Channels newChannel

  final implicit val `WritableByteChannel ~=> OutputStream`: WritableByteChannel ~⇒ OutputStream =
    Channels newOutputStream

  final implicit val `OutputStream ~=> WritableByteChannel`: OutputStream ~⇒ WritableByteChannel =
    Channels newChannel

  final implicit def `T: ByteSource ~=> ReadableByteChannel`[T: ByteSource]: T ~⇒ ReadableByteChannel =
    (self: T) ⇒ new ReadableByteChannel {
      def isOpen: Boolean = true
      def close(): Unit = ()
      def read(into: ByteBuffer): Int = ByteSource[T] readInto (self, into)
    }

  final implicit def `ReadableByteChannel ~=> ByteSource[T]`: ReadableByteChannel ~⇒ ByteSource[Any] =
    (channel: ReadableByteChannel) ⇒ new ByteSource[Any] {
      def readInto(source: Any, into: ByteBuffer): Int = channel read into
    }
}

object JavaIoConversions extends JavaIoConversions
