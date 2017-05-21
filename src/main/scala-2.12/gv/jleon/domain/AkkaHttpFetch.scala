package gv.jleon

import akka.http.scaladsl.{ HttpExt }
import akka.http.scaladsl.client.{ RequestBuilding }
import akka.stream.{ Materializer }
import akka.stream.scaladsl.{ Source }

final case class AkkaHttpFetch(
  akkaHttp:     HttpExt,
  materializer: Materializer
)

object AkkaHttpFetch {

  final implicit case object Interpretation extends Fetch.Interpretation[AkkaHttpFetch] {
    override def fetch(self: AkkaHttpFetch): Fetch.FetchFunc.t =
      (uri: Uri) ⇒ {
        implicit val _materializer = self.materializer
        Source
          .fromFuture(self.akkaHttp.singleRequest(RequestBuilding.Get(uri)))
          .flatMapConcat(_.entity.dataBytes)
      }
  }

}
