package gv.jleon

import scala.collection.JavaConverters._

import shapeless.{ HNil, :: }

import Config.{ RichConfigValue }

object MirrorFactory {

  final object key {
    val FetchStrategy = "fetchStrategy"
    val Mirrors = "mirrors"
  }

  /**
   * Yield `(mirror prefix ⇒ mirror config)
   *
   * @param self root ("mirrors") mirrors config
   */
  final implicit class MirrorsConfig(val self: Config.tsConfigObject) extends AnyVal {
    def entries: Traversable[(String, Config.tsConfig)] =
      self.asScala
        .map {
          case (name, value) ⇒
            value.asConfig.map { config ⇒ (name, config) }
        }
        .collect { case Some(p) ⇒ p }
  }

  /**
   * A mirror config (entry in "mirrors").
   *
   * @param self an entry under "mirrors"
   */
  final implicit class MirrorConfig(val self: Config.tsConfig) extends AnyVal {
    @inline def fetchStrategy: String = self getString key.FetchStrategy
    @inline def mirrors: Config.tsConfigObject = self getObject key.Mirrors
  }
}

import MirrorFactory._

trait MirrorFactory extends Any {

  private[this] def fetchStrategy(
    implicit
    config:          MirrorConfig,
    prefix:          Mirror.Prefix,
    fetchRepository: FetchRepository
  ): Option[Fetch] =
    fetchRepository(config.self getString key.FetchStrategy)

  private[this] def mirrors(
    implicit
    config: MirrorConfig,
    prefix: Mirror.Prefix
  ): Vector[Mirror] =
    config.self
      .getObject(key.Mirrors).asScala
      .map {
        case (name, configValue) ⇒
          configValue.asString.map { url ⇒
            Mirror(baseUrl = Uri(url), prefix = prefix, name = name)
          }
      }
      .collect { case Some(m) ⇒ m }
      .toVector

  private[this] def mirrorsWithFetchStrategy(
    implicit
    config:          MirrorConfig,
    prefix:          Mirror.Prefix,
    fetchRepository: FetchRepository
  ): Option[Vector[Mirror :: Fetch :: HNil]] =
    fetchStrategy.map { fetch ⇒
      mirrors map (_ :: fetch :: HNil)
    }

  def fromConfig(
    implicit
    config:          MirrorsConfig,
    fetchRepository: FetchRepository
  ): Map[Mirror.Prefix, Vector[Mirror :: Fetch :: HNil]] = {
    config.entries.map {
      case (prefixV, mirrorConfigV) ⇒
        implicit val prefix: Mirror.Prefix = prefixV
        implicit val mirrorConfig: MirrorConfig = mirrorConfigV
        mirrorsWithFetchStrategy map ((prefix, _))
    }
      .collect { case Some(ms) ⇒ ms }
      .toMap
  }

}
