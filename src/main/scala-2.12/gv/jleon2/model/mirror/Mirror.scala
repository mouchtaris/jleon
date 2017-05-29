package gv.jleon2
package model
package mirror

import scala.language.{ higherKinds }
import scala.concurrent.{ Future }

import shapeless.{ HNil, :: }

trait Mirror {

  type Prefix
  type Handler <: mirror.Handler

  //  def apply(prefix: Prefix): Future[Mirror#Prefix :: Mirror#Handler :: HNil]

  def apply(prefix: Prefix): Future[Handler]
}
