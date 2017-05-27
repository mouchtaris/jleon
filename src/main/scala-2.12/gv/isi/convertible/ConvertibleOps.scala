package gv.isi
package convertible

import scala.language.{ implicitConversions, higherKinds }

/**
 * Decorations for a convertible object.
 * Receives decoration methods by [[ConvertibleOps.Ops]],
 * for converting to other type, through implicit conversions.
 */
object ConvertibleOps {

  /**
   * Utility class, for when converting to effects.
   *
   * This is class is there to allow the user of [[Ops.convertToEffect]]
   * to need to specify only one type parameter.
   *
   * @param self the decorated "self"
   * @tparam S the "self" type
   * @tparam F the target effect
   */
  final implicit class ToEffectConvertible[S, F[_]](val self: S) extends AnyVal {
    @inline def apply[T, SF[_]]()(implicit ev1: S <:< SF[T], ctor: ConstructableFrom[SF[T]]#To[F[T]]): F[T] =
      ctor(ev1(self))
  }

  final object ToEffectConvertible {
    /**
     * Provide an implicit conversion of [[ToEffectConvertible]] to the target effect,
     * in case that a conversion is used like this:
     * {{{
     *   val target: Future[Int] = (o: Option[Int]).convertToEffect
     * }}}
     * @param tec to effect convertible (self)
     * @param ev1 evidence (1) that self type is an effect
     * @param ctor constructor from self (effect) to target effect
     * @tparam S self type
     * @tparam F target effect type
     * @tparam T effected type
     * @tparam SF self type, as an effect
     * @return self converted to target effect
     */
    @inline implicit def convert[S, F[_], T, SF[_]](
      tec: ToEffectConvertible[S, F]
    )(
      implicit
      ev1:  S <:< SF[T],
      ctor: ConstructableFrom[SF[T]]#To[F[T]]
    ): F[T] = tec.apply[T, SF]
  }

  /**
   * Decoration operations for a convertible.
   *
   * @tparam S The "self" type
   */
  trait Ops[S] extends Any {
    final type Self = S
    final type Constructor[T] = ConstructableFrom[Self]#To[T]

    protected[this] def self: Self

    /**
     * Convert to target type.
     * @tparam T target type
     * @return self converted to target type
     */
    @inline final def convertTo[T: Constructor]: T =
      implicitly[Constructor[T]].apply(self)

    /**
     * Convert to an effectful type.
     *
     * This method can be used like:
     * {{{
     *   val o: Option[Int] = Some(12)
     *   val f: Future[Int] = o.convertToEffect[Future]
     *   // or
     *   val f2 = o.convertToEffect[Future]()
     * }}}
     * @tparam F target effect
     * @return self converted to target type
     */
    @inline final def convertToEffect[F[_]]: ToEffectConvertible[S, F] = self

  }

}
