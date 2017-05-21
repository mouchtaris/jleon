package gv.jleon
package facade

import akka.actor.{ ActorSystem }
import akka.stream.{ Materializer, ActorMaterializer }
import akka.http.scaladsl.{ Http ⇒ AkkaHttp, HttpExt }

import shapeless.{ HNil, :: }

import config.{ Config }

final class JLeon()(
  implicit
  val actorSystem: ActorSystem = ActorSystem("JLeon"),
  val config:      Config      = Config("jleon")
) extends AnyRef
    with ImplicitConstructions {
  implicit val akkaHttpExt: HttpExt = createAkkaHttpExt
  implicit val materializer: Materializer = createAkkaActorMaterializer

  implicit val fetchStrategyRepository: FetchRepository = createFetchStrategyRepository

  val mirrors: Map[Mirror.Prefix, Vector[Mirror :: Fetch :: HNil]] = {
    implicit val mirrorConfig: mirror.MirrorsConfig = config.mirrors
    Mirror.fromConfig
  }
}

object JLeon {

  final object MIRROR_PREFIX {
    val ARCH = "arch"
    val all: Traversable[String] = Vector(ARCH)
  }

  final def apply(): JLeon = new JLeon()
}