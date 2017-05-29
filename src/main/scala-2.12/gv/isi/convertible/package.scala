package gv.isi

package object convertible extends AnyRef
    with ImplicitResolutionOrder.Conversions //    with ImplicitConversions
    {

  trait Conversion[A, B] {
    def apply(a: A): B
  }

  final type ~⇒[A, B] = Conversion[A, B]

  implicit class Convertible[S](override val self: S)
    extends AnyVal
    with ConvertibleOps.Ops[S]

}
