package gv
package isi
package typesafe.config

import com.typesafe.config.{ Config ⇒ TsConfig, ConfigObject ⇒ TsConfigObject, ConfigValue, ConfigValueType }

trait ConfigDecorationOps extends Any {
  def self: ConfigValue

  final def ifType(t: ConfigValueType): Option[Object] =
    if (self.valueType == t)
      Some(self.unwrapped)
    else
      None

  final def asString: Option[String] = ifType(ConfigValueType.STRING) map (_.asInstanceOf[String])

  final def asConfigObject: Option[TsConfigObject] = ifType(ConfigValueType.OBJECT) map (_ ⇒ self.asInstanceOf[TsConfigObject])

  final def asConfig: Option[TsConfig] = asConfigObject map (_.toConfig)
}
