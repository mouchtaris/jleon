package gv.isi
package std
package conversions

trait UriConversions extends Any {

  import akka.http.scaladsl.model.{ Uri ⇒ AkkaUri }
  import java.net.{ URI ⇒ JavaUri }

  final implicit def fromAkkaHttpUriToJavaNetUri: AkkaUri ⇒ JavaUri =
    akka ⇒ JavaUri.create(akka.toString)

  final implicit def fromJavaNetUriToAkkaHttpUri: JavaUri ⇒ AkkaUri =
    java ⇒ AkkaUri(java.toString)
}
