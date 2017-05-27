package gv.isi
package convertible

sealed trait ConstructableFrom[S] {
  final type To[T] = S ~⇒ T
}
