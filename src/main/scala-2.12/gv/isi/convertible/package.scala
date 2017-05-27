package gv.isi

package object convertible extends AnyRef
    with ImplicitResolutionOrder.Conversions {

  final implicit class Conversion[A, B](val self: A ⇒ B) extends AnyVal {
    @inline def apply(a: A): B = self(a)
  }

  final type ~⇒[A, B] = Conversion[A, B]

  sealed trait ConstructableFrom[S] {
    final type To[T] = S ~⇒ T
  }

  final implicit class Convertible[S](override val self: S)
      extends AnyVal
      with ConvertibleOps.Ops[S] {

    def asConvertible: Convertible[S] = this

  }

}
