package gv
package isi
package std.conversions

import scala.language.{ implicitConversions }
import scala.util.{ Try }
import scala.concurrent.{ Future }

import convertible.{ ~=> }

/**
 * Provide conversions from standard types to [[scala.concurrent.Future]].
 */
trait ToFutureConversions extends Any {

  @inline
  final implicit def `Try[T] ~=> Future[T]`[T]: Try[T] ~=> Future[T] = Future.fromTry _

}
