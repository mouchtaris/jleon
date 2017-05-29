package gv.isi
package std
package conversions

import scala.language.{ implicitConversions }

import _root_.java.{ net ⇒ jnet }

trait UriConversions extends Any {

  import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }
  import jnet.{ URI ⇒ JavaUri }

  @inline
  final implicit def fromAkkaHttpUriToJavaNetUri(akka: AkkaUri): JavaUri =
    JavaUri.create(akka.toString)

  @inline
  final implicit def fromJavaNetUriToAkkaHttpUri(java: JavaUri): AkkaUri =
    AkkaUri(java.toString)
}
