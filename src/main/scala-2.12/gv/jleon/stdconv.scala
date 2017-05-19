package gv.jleon

import java.{ net ⇒ jnet }

import scala.language.{ implicitConversions }

import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }

/**
 * Standard Conveniences
 */
trait stdconv {

  /**
   * Convert an Akka Uri to [[java.net.URI]].
   * @param uri
   * @return
   */
  final implicit def toJavaNetUri(uri: AkkaUri): jnet.URI = jnet.URI.create(uri.toString)

  /**
   * Convert a [[java.net.URI]] to Akka Uri.
   * @param uri
   * @return
   */
  final implicit def toAkkaUri(uri: jnet.URI): AkkaUri = AkkaUri(uri.toString)

  /**
   * Mark a generic argument as implicitly provided
   */
  type Implicitly[T] = T
}

object stdconv extends stdconv
