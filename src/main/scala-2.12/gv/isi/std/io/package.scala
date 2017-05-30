package gv
package isi
package std

package object io extends AnyRef {

  final implicit class ReadCompletelyDecoration[T](val self: T)
    extends AnyVal
    with ReadCompletely[T]

}
