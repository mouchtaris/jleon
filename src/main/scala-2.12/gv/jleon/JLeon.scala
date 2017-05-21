package gv.jleon

import scala.collection.JavaConverters._

import akka.actor.{ ActorSystem }

import com.typesafe.config.{
  Config ⇒ tsConfig,
  ConfigFactory
}

final class JLeon()(
    implicit
    val actorSystem: ActorSystem  = ActorSystem("JLeon"),
    val config:      JLeon.Config = ConfigFactory.defaultApplication().getConfig("jleon")
) {
  val mirrors: Map[String, Traversable[Mirror]] = JLeon.loadMirrors
}

object JLeon {

  final object MIRROR_PREFIX {
    val ARCH = "arch"
    val all: Traversable[String] = Vector(ARCH)
  }

  final implicit class Config(val self: tsConfig) extends AnyVal {
    def mirror(prefix: String): Stream[Mirror] =
      self.getStringList(s"mirror.$prefix").asScala.toStream
        .map(url ⇒ Mirror(prefix = prefix, baseUrl = Uri(url)))
  }

  final def loadMirrors(implicit config: Config): Map[String, Traversable[Mirror]] = {
    MIRROR_PREFIX.all
      .map { prefix ⇒ (prefix, config.mirror(prefix)) }
      .toMap
  }

}
