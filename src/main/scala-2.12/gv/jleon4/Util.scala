package gv.jleon4

object UtilValueClasses {
  final case object CouldBeSingleton

  implicit class CouldBe[T](val self: CouldBeSingleton.type) extends AnyVal {
    final type t[a] = a ⇒ T

    def apply[U](u: U)(implicit ev: U ⇒ T): U = u
  }
}

trait Util {
  final def pf[T](t: ⇒ T): Any ~~> T = { case _ ⇒ t }

  /** syntax-require: block is never run */
  final def squire(t: ⇒ Any): Unit = ()

  type CouldBe[T] = UtilValueClasses.CouldBe[T]
  final def couldBe[T]: CouldBe[T] = UtilValueClasses.CouldBeSingleton
}

object Util extends Util
