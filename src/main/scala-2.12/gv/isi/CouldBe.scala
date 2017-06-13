package gv
package isi

object CouldBeValues {

  case object CouldBeSingleton

  implicit class CouldBe[T](val self: CouldBeSingleton.type) extends AnyVal {
    final type t[a] = a ⇒ T

    def apply[U](u: U)(implicit ev: U ⇒ T): U = u
  }

}

trait CouldBe {

  final type CouldBe[T] = CouldBeValues.CouldBe[T]
  final val CouldBeSingleton = CouldBeValues.CouldBeSingleton

  final def couldBe[T]: CouldBe[T] = CouldBeSingleton

}
