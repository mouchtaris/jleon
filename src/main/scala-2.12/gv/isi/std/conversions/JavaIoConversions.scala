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

import io.{ ByteSourceDecoration }
import convertible.{ ~⇒ }

private[this] case object Imports extends AnyRef
  with functional.Unfold
import Imports._

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
    (channel: ReadableByteChannel) ⇒ (sourceNotUsed: Any, into: ByteBuffer) ⇒ channel read into

  final implicit def `ByteSource ~⇒ Stream[ByteBuffer]`[BS: ByteSource]: BS ~⇒ Stream[ByteBuffer] =
    source ⇒ unfold(()) { _ ⇒
      val buffer = ByteBuffer.allocate(8 << 10)
      buffer.clear()
      val read = source.read(buffer)
      buffer.flip()
      if (read == -1)
        None
      else
        Some { ((), buffer) }
    }

}
