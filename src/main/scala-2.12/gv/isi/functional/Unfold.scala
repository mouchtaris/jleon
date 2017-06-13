package gv
package isi.functional

trait Unfold extends Any {

  def unfold[A, B](a: A)(f: A ⇒ Option[(A, B)]): Stream[B] =
    f(a) map { case (a, b) ⇒ b #:: unfold(a)(f) } getOrElse Stream.empty

}
