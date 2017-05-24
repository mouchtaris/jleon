package gv.jleon
package facade

import shapeless.{ HNil }

import config.{ Config }

final class JLeon()(
  implicit
  val actorSystem: ActorSystem = ActorSystem("JLeon"),
  val config:      Config      = Config("jleon")
) extends AnyRef
    with ImplicitConstructions {

  implicit val akkaHttpExt: HttpExt = createAkkaHttpExt
  implicit val materializer: Materializer = createAkkaActorMaterializer

  implicit val fetchStrategyRepository: domain.FetchRepository = createFetchStrategyRepository

  val mirrors: MirrorRepository = {
    implicit val mirrorConfig: MirrorsConfig = config.mirrors
    Mirror.fromConfig
  }

  val storage: LockingStorage = {
    implicit val storageConfig: StorageConfig = config.storage
    Storage.fromConfig
  }

  val fetchManager: FetchManager = mirrors :: storage :: HNil

  @inline def shutdown(): Future[akka.actor.Terminated] = actorSystem.terminate()
}

object JLeon {

  final object MIRROR_PREFIX {
    val ARCH = "arch"
    val all: Traversable[String] = Vector(ARCH)
  }

  final def apply(): JLeon = new JLeon()
}
