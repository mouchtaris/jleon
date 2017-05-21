package gv.jleon
package config

import com.typesafe.config.{ ConfigValue, ConfigValueType }

protected[config] trait RichConfigValueOps extends Any {

  def self: ConfigValue

  final def ifType(t: ConfigValueType): Option[Object] =
    if (self.valueType == t)
      Some(self.unwrapped)
    else
      None

  final def asString: Option[String] = ifType(ConfigValueType.STRING) map (_.asInstanceOf[String])

  final def asConfigObject: Option[tsConfigObject] = ifType(ConfigValueType.OBJECT) map (_ ⇒ self.asInstanceOf[tsConfigObject])

  final def asConfig: Option[tsConfig] = asConfigObject map (_.toConfig)
}
