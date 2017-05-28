package gv.jleon2
package model
package mirror

import scala.language.{ higherKinds }
import scala.concurrent.{ Future }

trait MirrorRepository {

  type Prefix
  type Mirror <: mirror.Mirror

  def apply(prefix: Prefix): Future[Mirror]

}
