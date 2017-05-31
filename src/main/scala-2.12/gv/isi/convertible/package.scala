package gv.isi

package object convertible extends AnyRef
    with Conversions {

  final type ~=>[A, B] = Conversion[A, B]

  final type ~⇒[A, B] = A ~=> B

  implicit class Convertible[S](override val self: S)
    extends AnyVal
    with ConvertibleOps.Ops[S]

}
