package gv.isi
package std.conversions

import scala.util.{ Try }
import scala.concurrent.{ Future }

import convertible.{ ~⇒ }

/**
 * Provide conversions from standard types to [[scala.concurrent.Future]].
 */
trait ToFutureConversions extends Any {

  final implicit def fromTry[T]: Try[T] ~⇒ Future[T] = Future.fromTry[T] _

}
