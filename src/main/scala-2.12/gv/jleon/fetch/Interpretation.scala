package gv.jleon
package fetch

import akka.{ NotUsed }
import akka.stream.scaladsl.{ Source }
import akka.util.{ ByteString }

trait Interpretation[T] extends Any {

  def fetch(self: T, uri: Uri): Source[ByteString, NotUsed]

}

