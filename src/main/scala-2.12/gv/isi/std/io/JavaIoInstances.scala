package gv
package isi
package std.io

import java.nio.{ ByteBuffer }
import java.nio.channels.{ ReadableByteChannel }

import isi.io.{ ByteSource }
import isi.convertible.{ ~⇒, Convertible }

trait JavaIoInstances extends AnyRef {

  final implicit object ReadableByteChannelSource extends ByteSource[ReadableByteChannel] {
    @inline
    def readInto(source: ReadableByteChannel, into: ByteBuffer): Int =
      source read into
  }

  /**
   * Anything that can be converted to a ReadableByteChannel, has a ByteSource
   * interpretation as well.
   */
  final implicit def convertibleToReadableByteChannelByteSource[T](implicit conv: T ~⇒ ReadableByteChannel): ByteSource[T] =
    (source: T, into: ByteBuffer) ⇒ ReadableByteChannelSource readInto (source.convertTo, into)

}
