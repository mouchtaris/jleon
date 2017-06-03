package gv
package isi
package typesafe

import com.typesafe.config.{ ConfigValue }

package object config {

  final implicit class ConfigDecoration(val self: ConfigValue) extends AnyVal with ConfigDecorationOps

}
