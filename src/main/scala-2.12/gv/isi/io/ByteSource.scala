package gv
package isi
package io

import java.nio.{ ByteBuffer }

trait ByteSource[T] {

  /**
   * Respective of a ReadableByteChannel#read
   *
   * @param source something like a ReadableByteChannel
   * @param into a byte buffer
   * @return the number of read bytes, possibly 0, or -1 for end of stream
   */
  def readInto(source: T, into: ByteBuffer): Int

}

object ByteSource {

  @inline
  def apply[T: ByteSource]: ByteSource[T] = implicitly

}
