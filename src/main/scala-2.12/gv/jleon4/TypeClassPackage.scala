package gv
package jleon4

import language.{ higherKinds }

trait TypeClassPackage {

  trait TypeClass[-Self] extends Any {
    type Out

    def apply(self: Self): Out
  }

  object TypeClass {
    trait WithTypeParams[-Self, out] extends Any with TypeClass[Self] {
      final type Out = out
    }
  }

  trait TypeClassCompanion[TC[T] <: TypeClass[T]] {
    def apply[T: TC]: TC[T] = implicitly
    def apply[T](self: T)(implicit tc: TC[T]): tc.Out = tc(self)
  }

}
