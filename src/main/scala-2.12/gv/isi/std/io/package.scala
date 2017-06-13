package gv
package isi
package std

package object io extends AnyRef
  with ByteSourceInstances
  with ByteSinkInstances
{

  implicit class ReadCompletelyDecoration[T](val self: T) extends AnyVal with ReadCompletely[T]
  implicit class WriteCompletelyDecoration[T](val self: T) extends AnyVal with WriteCompletely[T]

  implicit class ByteSinkDecoration[T](val self: T) extends AnyVal with ByteSinkDecorationOps[T]
  implicit class ByteSourceDecoration[T](val self: T) extends AnyVal with ByteSourceDecorationOps[T]

}
