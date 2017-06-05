package gv
package isi

trait FunctionPackage {

  trait Function extends Any {
    type FunctionIn
    type FunctionOut

    def apply(a: FunctionIn): FunctionOut
  }

  final object Function {
    type Aux[a, b] = Function {
      type FunctionIn = a
      type FunctionOut = b
    }
  }
}
