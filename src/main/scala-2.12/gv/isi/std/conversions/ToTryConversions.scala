package gv
package isi
package std.conversions

import scala.util.{ Try, Success, Failure }

import convertible.{ ~=> }

/**
 * Provide conversions from standard type to [[scala.util.Try]].
 */
trait ToTryConversions extends Any {

  final implicit def `Option[T] ~=> Try[T]`[T]: Option[T] ~=> Try[T] = {
    case Some(v) ⇒ Success(v)
    case None    ⇒ Failure(new NoSuchElementException("Option.None"))
  }

}
