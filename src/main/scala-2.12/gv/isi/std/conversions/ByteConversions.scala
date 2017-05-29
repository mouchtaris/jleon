package gv
package isi
package std.conversions

import language.{ implicitConversions }

import _root_.java.{ nio ⇒ jnio }
import jnio.{ ByteBuffer }
import jnio.charset.{ Charset, StandardCharsets }

import convertible.{ ~⇒ }

trait ByteConversions extends AnyRef {

  final val defaultCharset: Charset = StandardCharsets.UTF_8

  @inline
  final implicit def stringToByteArrayConversion(implicit charset: Charset = defaultCharset): String ~⇒ Array[Byte] = _ getBytes charset

  final implicit val byteArrayToByteBufferConversion: Array[Byte] ~⇒ ByteBuffer = ByteBuffer wrap _
}

object ByteConversions extends ByteConversions
