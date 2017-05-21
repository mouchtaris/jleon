package gv.jleon

import com.typesafe.config.{ ConfigValue, ConfigValueType }

object Config {

  final type tsConfig = com.typesafe.config.Config
  final type tsConfigObject = com.typesafe.config.ConfigObject

  final object key {
    val mirrors = "mirrors"
  }

  final implicit class Config(val self: tsConfig) extends AnyVal {
    def apply(path: String): tsConfig = self getConfig path
    def mirrors: tsConfigObject = self getObject s"${key.mirrors}"
  }

  def apply(): Config =
    com.typesafe.config.ConfigFactory.defaultApplication()

  def apply(defaultNamespace: String): Config =
    this()(defaultNamespace)

  final implicit class RichConfigValue(val self: ConfigValue) extends AnyVal {
    def ifType(t: ConfigValueType): Option[Object] =
      if (self.valueType == t)
        Some(self.unwrapped)
      else
        None

    def asString: Option[String] = ifType(ConfigValueType.STRING) map (_.asInstanceOf[String])
    def asConfigObject: Option[tsConfigObject] = ifType(ConfigValueType.OBJECT) map (_.asInstanceOf[tsConfigObject])
    def asConfig: Option[tsConfig] = asConfigObject map (_.toConfig)
  }

}
