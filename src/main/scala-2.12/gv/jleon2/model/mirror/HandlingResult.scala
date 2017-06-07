package gv.jleon2
package model
package mirror

sealed trait HandlingResult

object HandlingResult {
  final case class Found() extends HandlingResult

  final case class Error(msg: String, cause: Throwable) extends Exception(msg, cause)
}
