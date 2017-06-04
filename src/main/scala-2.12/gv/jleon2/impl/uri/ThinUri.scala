package gv
package jleon2
package impl.uri

import language.{ implicitConversions }

import java.net.{ URI ⇒ Java }

import akka.http.scaladsl.model.{ Uri ⇒ Akka }

/**
 * A thin Uri wrapper than can be any library provided URI.
 */
final case class ThinUri(uri: String)

object ThinUri {
  @inline
  implicit def javaUri(uri: ThinUri): Java = Java create uri.uri

  @inline
  implicit def akkaUri(uri: ThinUri): Akka = Akka apply uri.uri
}
