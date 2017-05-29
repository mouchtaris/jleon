package gv
package isi
package std

package object io extends AnyRef
    with JavaIoInstances {

  final implicit class ReadCompletelyDecoration[T](val self: T)
    extends AnyVal
    with ReadCompletely[T]

}
