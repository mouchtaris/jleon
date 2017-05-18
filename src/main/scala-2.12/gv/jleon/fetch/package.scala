package gv.jleon

package object fetch {

  final implicit class Fetch[T](override val self: T) extends AnyVal with Ops[T]

}
