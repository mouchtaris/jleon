package gv
package isi
package std.io

import language.{ postfixOps }

import java.{ nio ⇒ jnio }
import java.{ util ⇒ jutil }

import jnio.channels.{ ReadableByteChannel }
import jnio.charset.{ Charset }
import jnio.{ ByteBuffer }
import jutil.{ Scanner }

import io.{ ByteSource }
import convertible.{ Convertible }
import std.conversions._

trait ReadCompletely[T] extends Any {

  protected[this] def self: T

  final def readCompletely(charset: Charset = defaultCharset)(implicit bs: ByteSource[T]): ByteBuffer = {
    val scanner: Scanner =
      new Scanner(
        self.convertTo[ReadableByteChannel],
        charset.name
      ) useDelimiter "\\A"

    val content: String = if (scanner.hasNext()) scanner.next() else ""

    content.convertTo[ByteBuffer]
  }

}
