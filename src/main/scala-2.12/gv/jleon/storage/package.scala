package gv.jleon

package object storage {

  final implicit class Storage[T](override val self: T) extends AnyVal with Ops[T]

}
