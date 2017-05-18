package gv.jleon
package fetch

import akka.stream.scaladsl.{ Source }
import akka.util.{ ByteString }
import akka.{ NotUsed }

trait Ops[T] extends Any {

  type I = Interpretation[T]

  protected[this] def self: T

  def fetch(uri: Uri)(implicit i: I): Source[ByteString, NotUsed] = i.fetch(self, uri)

}
