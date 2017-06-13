package gv
package isi
package std.io

import java.{ nio ⇒ jnio }

import jnio.{ ByteBuffer }

import io.{ ByteSink ⇒ BS }
import functional.{ Unfold }

private[this] case object ThisImports extends AnyRef
  with Unfold

import ThisImports._

trait WriteCompletely[T] extends Any {

  protected[this] def self: T

  final def writeCompletely(content: ByteBuffer)(implicit bs: BS[T]): Int =
    unfold(()) { _ ⇒
      if (content.remaining() > 0)
        Some(((), self.write(content)))
      else
        None
    }.sum

}
