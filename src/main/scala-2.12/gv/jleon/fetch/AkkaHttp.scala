package gv.jleon
package fetch

import akka.{ NotUsed }
import akka.http.scaladsl.{ HttpExt }
import akka.http.scaladsl.client.{ RequestBuilding }
import akka.stream.{ Materializer }
import akka.stream.scaladsl.{ Source }
import akka.util.{ ByteString }

final case class AkkaHttp(
  akkaHttp:     HttpExt,
  materializer: Materializer
)

object AkkaHttp {

  final implicit case object Interpretation extends Interpretation[AkkaHttp] {
    override def fetch(self: AkkaHttp, uri: Uri): Source[ByteString, NotUsed] = {
      implicit val _materializer = self.materializer
      Source
        .fromFuture(self.akkaHttp.singleRequest(RequestBuilding.Get(uri)))
        .flatMapConcat(_.entity.dataBytes)
    }
  }

}
