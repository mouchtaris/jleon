package gv.jleon2
package model
package storage

import java.nio.channels.{ ReadableByteChannel, WritableByteChannel }

sealed trait LockResult

object LockResult {

  // Used as future errors
  final case class Locked(cause: Throwable) extends Exception("locked", cause)
  final case class Failed(cause: Throwable) extends Exception("failed", cause)

  final case class Acquired(channel: WritableByteChannel) extends AnyRef with LockResult
  final case class Found(channel: ReadableByteChannel) extends AnyRef with LockResult
}
