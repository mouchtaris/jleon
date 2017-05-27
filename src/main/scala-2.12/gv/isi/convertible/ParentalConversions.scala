package gv.isi
package convertible

/**
 * Provide implicit conversions to parent types.
 */
trait ParentalConversions extends Any {

  //  final implicit def parentalConversion[P, C <: P]: C ~⇒ P = (c: C) ⇒ c: P

}
