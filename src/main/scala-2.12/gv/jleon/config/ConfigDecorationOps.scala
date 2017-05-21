package gv.jleon
package config

protected[config] trait ConfigDecorationOps extends Any {

  def self: tsConfig

  final def apply(path: String): tsConfig = self getConfig path

  final def mirrors: tsConfigObject = self getObject s"${config.key.mirrors}"

  final def storage: tsConfig = self getConfig s"${config.key.storage}"

}
