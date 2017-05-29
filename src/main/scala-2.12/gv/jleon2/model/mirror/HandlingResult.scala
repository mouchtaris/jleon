package gv.jleon2
package model
package mirror

import java.nio.channels.{ ReadableByteChannel }

sealed trait HandlingResult

object HandlingResult {
  final case class Found(channel: ReadableByteChannel) extends HandlingResult
}
