package gv.isi.functional

/**
  * Allow the following trick:
  * {{{
  *   trait F[T] {
  *     type R
  *     def f(self: T): R
  *   }
  *
  *   implicit final object FInt extends F[Int] {
  *     type R = Int
  *     def f(self: Int): Int = self + 1
  *   }
  *
  *   val bundle = (FInt, 12)
  *
  *   println { // Prints 13
  *     bundle method (_.f)
  *   }
  *
  * }}}
  */
object TupledCall {

  final implicit class Decoration[I, S](val interpSelf: (I, S)) extends AnyVal {
    def method[R](getf: I ⇒ S ⇒ R): R = interpSelf match {
      case (interp, self) ⇒ getf(interp)(self)
    }
  }
}
