package gv
package isi
package std.io

import java.nio.{ ByteBuffer }

import io.{ ByteSource }

trait ByteSourceDecorationOps[T] extends Any {

  def self: T

  @inline
  final def read(into: ByteBuffer)(implicit source: ByteSource[T]): Int =
    source readInto (self, into)

}
