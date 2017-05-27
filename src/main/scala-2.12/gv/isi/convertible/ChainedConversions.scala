package gv.isi
package convertible

/**
 * Provide conversions if there is a chained conversion available.
 */
trait ChainedConversions extends Any {

  final implicit def chainedConversion[A, B, C](implicit a2b: A ~⇒ B, b2c: B ~⇒ C): A ~⇒ C =
    a2b.self andThen b2c.self

}
