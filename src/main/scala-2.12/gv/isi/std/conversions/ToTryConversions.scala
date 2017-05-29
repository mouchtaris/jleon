package gv.isi
package std.conversions

import scala.language.{ implicitConversions }
import scala.util.{ Try, Success, Failure }

/**
 * Provide conversions from standard type to [[scala.util.Try]].
 */
trait ToTryConversions extends Any {

  final implicit def fromOption[T](opt: Option[T]): Try[T] = opt match {
    case Some(v) ⇒ Success(v)
    case None    ⇒ Failure(new NoSuchElementException("Option.None"))
  }

}
