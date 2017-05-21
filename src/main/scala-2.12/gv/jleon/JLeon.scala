package gv.jleon

import akka.actor.{ ActorSystem }
import akka.stream.{ Materializer, ActorMaterializer }
import akka.http.scaladsl.{ Http ⇒ AkkaHttp, HttpExt }

import shapeless.{ HNil, :: }

final class JLeon()(
  implicit
  val actorSystem: ActorSystem = ActorSystem("JLeon"),
  val config:      Config      = Config("jleon")
) extends AnyRef
    with JLeon.ImplicitConstructions {
  implicit val akkaHttpExt: HttpExt = createAkkaHttpExt
  implicit val materializer: Materializer = createAkkaActorMaterializer

  implicit val fetchStrategyRepository: FetchRepository = createFetchStrategyRepository

  val mirrors: Map[Mirror.Prefix, Vector[Mirror :: Fetch :: HNil]] = {
    implicit val mirrorConfig: MirrorFactory.MirrorsConfig = config.mirrors
    Mirror.fromConfig
  }
}

object JLeon {

  final object MIRROR_PREFIX {
    val ARCH = "arch"
    val all: Traversable[String] = Vector(ARCH)
  }

  trait ImplicitConstructions extends Any {

    protected[this] final def createAkkaHttpExt(implicit as: ActorSystem): HttpExt =
      AkkaHttp()

    protected[this] final def createAkkaActorMaterializer(implicit as: ActorSystem): ActorMaterializer =
      ActorMaterializer()

    protected[this] final def createAkkaHttpFetchStrategy(implicit http: HttpExt, mat: Materializer): AkkaHttpFetch =
      AkkaHttpFetch(http, mat)

    protected[this] final def createFetchStrategyRepository(implicit http: HttpExt, mat: Materializer): FetchRepository = Map {
      "akkaHttp" → Fetch(createAkkaHttpFetchStrategy)
    }

  }

  final def apply(): JLeon = new JLeon()
}
