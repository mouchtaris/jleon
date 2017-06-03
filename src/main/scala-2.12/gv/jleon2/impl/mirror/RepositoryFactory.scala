package gv
package jleon2
package impl.mirror

final case class RepositoryFactory(
  config: impl.mirror.Config
) extends AnyRef
  with model.mirror.RepositoryFactory {

  type Config = impl.mirror.Config
  type Repository = impl.mirror.Repository

  private[this] val handlers: Map[Repository # Prefix, Repository # Handler] = {
    import isi.typesafe.config._
    import collection.JavaConverters._
    config.config.entrySet.asScala
      .map { entry ⇒ (entry.getKey, entry.getValue) }
      .map { case (key, value) ⇒ value.asConfig.map { config ⇒ (key, config) } }
      .collect { case Some(t) ⇒ t }
      .map { case (key, value) ⇒ (key, impl.mirror.Handler()) }
      .toMap
  }

  def apply(config: Config): Repository = {
//    impl.mirror.Repository(config)
    ??? // asd
  }

}
