package gv.jleon2
package model
package storage

import java.nio.channels.{ ReadableByteChannel, WritableByteChannel }

sealed trait LockResult

object LockResult {
  final case class Locked(cause: Throwable) extends Exception("locked", cause) with LockResult
  final case class Failed(cause: Throwable) extends Exception("failed", cause) with LockResult
  final case class Acquired(channel: WritableByteChannel) extends AnyRef with LockResult
  final case class Found(channel: ReadableByteChannel) extends AnyRef with LockResult
}
