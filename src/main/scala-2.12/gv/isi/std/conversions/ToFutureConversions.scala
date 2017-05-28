package gv.isi
package std.conversions

import scala.language.{ implicitConversions }
import scala.util.{ Try }
import scala.concurrent.{ Future }

/**
 * Provide conversions from standard types to [[scala.concurrent.Future]].
 */
trait ToFutureConversions extends Any {

  @inline
  final implicit def fromTry[T](t: Try[T]): Future[T] = Future.fromTry(t)

}
