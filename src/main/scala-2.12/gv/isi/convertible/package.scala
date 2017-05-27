package gv.isi

package object convertible extends AnyRef
    with ImplicitResolutionOrder.Conversions {

  implicit class Conversion[A, B](override val self: A ⇒ B)
    extends AnyVal
    with ConversionOps.Ops[A, B]

  final type ~⇒[A, B] = Conversion[A, B]

  implicit class Convertible[S](override val self: S)
    extends AnyVal
    with ConvertibleOps.Ops[S]

}
