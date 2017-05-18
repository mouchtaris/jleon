package gv.jleon

import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.directives.{ DebuggingDirectives }
import akka.http.scaladsl.server.{ Directives, Route }
import akka.http.scaladsl.{ Http ⇒ AkkaHttp }

object Http {
  import Directives.{ _ }

  val route: Route = DebuggingDirectives.logRequestResult("leon") {
    pathPrefix("arch" / RemainingPath) { path ⇒ println(s"Asking for $path"); complete("") }
  }

  private[this] implicit def routeHandlingMaterializer(implicit actorSystem: ActorSystem): ActorMaterializer =
    ActorMaterializer()

  def serve(implicit actorSystem: ActorSystem): Future[AkkaHttp.ServerBinding] = {
    AkkaHttp().bindAndHandle(
      route,
      interface = "0.0.0.0",
      port      = 14000
    )
  }
}

