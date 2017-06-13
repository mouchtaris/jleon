package gv
package isi
package std.io

import java.nio.{ ByteBuffer }
import java.nio.channels.{ WritableByteChannel }

import isi.io.{ ByteSink ⇒ BS }
import isi.convertible.{ ~⇒, Convertible }

trait ByteSinkInstances extends AnyRef {

  final implicit object WritableByteChannelSource extends BS[WritableByteChannel] {
    @inline
    def writeFrom(dest: WritableByteChannel, from: ByteBuffer): Int =
      dest write from
  }

  /**
   * Anything that can be converted to a ReadableByteChannel, has a ByteSink
   * interpretation as well.
   */
  final implicit def `T ~=> WritableByteChanel: ByteSource`[T](implicit conv: T ~⇒ WritableByteChannel): BS[T] =
    (dest, from) ⇒ WritableByteChannelSource writeFrom (dest.convertTo[WritableByteChannel], from)

  final implicit object `ByteBuffer: ByteSink` extends BS[ByteBuffer] {
    @inline
    def writeFrom(dest: ByteBuffer, from: ByteBuffer): Int =
      `ByteBuffer: ByteSource` readInto (from, dest)
  }
}
