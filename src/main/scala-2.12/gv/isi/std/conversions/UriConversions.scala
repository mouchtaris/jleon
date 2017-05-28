package gv.isi
package std
package conversions

import scala.language.{ implicitConversions }

trait UriConversions extends Any {

  import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }
  import java.net.{ URI ⇒ JavaUri }

  @inline
  final implicit def fromAkkaHttpUriToJavaNetUri(akka: AkkaUri): JavaUri =
    JavaUri.create(akka.toString)

  @inline
  final implicit def fromJavaNetUriToAkkaHttpUri(java: JavaUri): AkkaUri =
    AkkaUri(java.toString)
}
