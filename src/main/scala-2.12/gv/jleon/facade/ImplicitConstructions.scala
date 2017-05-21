package gv.jleon
package facade

import akka.actor.{ ActorSystem }
import akka.http.scaladsl.{ Http ⇒ AkkaHttp, HttpExt }
import akka.stream.{ ActorMaterializer, Materializer }

protected[facade] trait ImplicitConstructions extends Any {

  protected[this] final def createAkkaHttpExt(implicit as: ActorSystem): HttpExt =
    AkkaHttp()

  protected[this] final def createAkkaActorMaterializer(implicit as: ActorSystem): ActorMaterializer =
    ActorMaterializer()

  protected[this] final def createAkkaHttpFetchStrategy(implicit http: HttpExt, mat: Materializer): AkkaHttpFetch =
    AkkaHttpFetch(http, mat)

  protected[this] final def createFetchStrategyRepository(implicit http: HttpExt, mat: Materializer): domain.FetchRepository = Map {
    "akkaHttp" → Fetch(createAkkaHttpFetchStrategy)
  }

}
