package gv
package jleon

import functional._

object Main {

  final implicit class MyVector[T](val v: Vector[T]) extends AnyVal {
    override def toString: String = v.toString
  }

  final implicit def myVectorMonad: Monad[MyVector] =
    new Monad[MyVector] {
      override def flatMap[A, B]: (A ⇒ MyVector[B]) ⇒ MyVector[A] ⇒ MyVector[B] =
        f ⇒ mv ⇒ implicitly[Monad[Vector]].flatMap(f(_: A).v)(mv.v)

      override def point[A]: (A) ⇒ MyVector[A] =
        a ⇒ implicitly[Monad[Vector]].point(a)
}

  def main(args: Array[String]): Unit = {
    println {
      implicitly[monoid.Addition[Vector[Int]]].op(Vector(1, 2, 3), Vector(4, 5, 6))
    }

    println {
      for {
        a ← MyVector { Vector(12, 13, 14) }
        b ← MyVector { Vector(1, 2, 3) }
      }
        yield a + b
    }
  }

}












