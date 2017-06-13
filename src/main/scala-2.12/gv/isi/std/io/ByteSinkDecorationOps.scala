package gv
package isi
package std.io

import java.nio.{ ByteBuffer }

import isi.io.{ ByteSink }

trait ByteSinkDecorationOps[T] extends Any {

  def self: T

  @inline
  final def write(from: ByteBuffer)(implicit sink: ByteSink[T]): Int =
    sink writeFrom (self, from)

}
