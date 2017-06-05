package gv
package jleon2
package model.uri

import language.{ implicitConversions }

trait Uri {

  // Inputs
  type Uri <: {
    def toJava: java.net.URI
    def toAkka: akka.http.scaladsl.model.Uri
  }
}
