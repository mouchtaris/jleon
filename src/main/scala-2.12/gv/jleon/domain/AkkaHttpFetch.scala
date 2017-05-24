package gv.jleon

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
          .flatMapConcat { response ⇒
            response.status match {
              case StatusCodes.Success(_) ⇒ response.entity.dataBytes
              case _                      ⇒ Source.failed(new IllegalStateException(s"HTTP response: $response"))
            }
          }
      }
  }

}
