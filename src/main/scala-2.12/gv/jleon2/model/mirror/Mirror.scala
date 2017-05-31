package gv.jleon2
package model
package mirror

import concurrent.{ Future }

trait Mirror {

  // Inputs
  type Prefix

  // Outputs
  type Handler <: mirror.Handler

  // TODO
  //  def apply(prefix: Prefix): Future[Mirror#Prefix :: Mirror#Handler :: HNil]

  def apply(prefix: Prefix): Future[Handler]
}
