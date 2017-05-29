package gv.isi
package convertible

/**
 * Provide priority ordering for resolving generic conversions last.
 */
object ImplicitResolutionOrder {

  trait P0 extends Any
  trait P25 extends Any with P0
    with ChainedConversions
  trait P50 extends Any with P25
  trait P100 extends Any with P50

  trait Conversions extends Any with P100

}
