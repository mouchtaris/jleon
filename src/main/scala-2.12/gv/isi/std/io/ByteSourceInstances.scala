package gv
package isi
package std.io

import java.nio.{ ByteBuffer }
import java.nio.channels.{ ReadableByteChannel }

import isi.io.{ ByteSource ⇒ BS }
import isi.convertible.{ ~⇒, Convertible }

trait ByteSourceInstances extends AnyRef {

  final implicit object ReadableByteChannelSource extends BS[ReadableByteChannel] {
    @inline
    def readInto(source: ReadableByteChannel, into: ByteBuffer): Int =
      source read into
  }

  /**
   * Anything that can be converted to a ReadableByteChannel, has a ByteSource
   * interpretation as well.
   */
  final implicit def `T ~=> ReadableByteChanel: ByteSource`[T](implicit conv: T ~⇒ ReadableByteChannel): BS[T] =
    (source, into) ⇒ ReadableByteChannelSource readInto (source.convertTo[ReadableByteChannel], into)

  final implicit object `ByteBuffer: ByteSource` extends BS[ByteBuffer] {
    @inline
    def readInto(source: ByteBuffer, into: ByteBuffer): Int = {
      val copied: Int = into.remaining
      val src: ByteBuffer = {
        val slice = source.slice
        slice.limit(copied)
        slice
      }
      into.put(src)

      copied
    }
  }
}
