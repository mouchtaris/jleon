package gv
package isi
package io

import java.nio.{ ByteBuffer }

trait ByteSink[T] {

  /**
    * Respective of a WritableByteChannel#read
    *
    * @param dest something like a WritableByteChannel
    * @param from a byte buffer
    * @return the number of read bytes, possibly 0, or -1 for end of stream
    */
  def writeFrom(dest: T, from: ByteBuffer): Int

}

object ByteSink {

  @inline
  def apply[T: ByteSink]: ByteSink[T] = implicitly

}
