package gv.jleon

import com.typesafe.config.{ ConfigValue, ConfigFactory }

package object config extends AnyRef
    with TypeAlias {

  protected[config] final object key {
    val mirrors = "mirrors"
  }

  final object Config {
    def apply(): Config = ConfigFactory.defaultApplication()

    def apply(defaultNamespace: String): Config = this()(defaultNamespace)
  }

  implicit class Config(override val self: tsConfig) extends AnyVal with ConfigDecorationOps

  final implicit class RichConfigValue(val self: ConfigValue) extends AnyVal with RichConfigValueOps

}
