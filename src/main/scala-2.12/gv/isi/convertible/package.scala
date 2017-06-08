package gv.isi

package object convertible extends AnyRef
    with ConvertiblePackage
    with Conversions {

  implicit class Convertible[S](override val self: S)
    extends AnyVal
    with ConvertibleOps.Ops[S]

}
