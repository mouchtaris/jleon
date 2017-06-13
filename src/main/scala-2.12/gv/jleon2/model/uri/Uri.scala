package gv
package jleon2
package model.uri

//import language.{ implicitConversions }

trait Uri extends Uri.Types {
  def toJava: java.net.URI
  def toAkka: akka.http.scaladsl.model.Uri
}

object Uri {
  trait Types extends Any {
  }
}
