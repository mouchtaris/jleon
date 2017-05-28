package gv.isi
package convertible

/**
 * Provide priority ordering for resolving generic conversions last.
 */
object ImplicitResolutionOrder {

  trait P0 extends AnyRef
  trait P25 extends P0 with ChainedConversions
  trait P50 extends P25 with ImplicitConversions
  trait P100 extends P50

  trait Conversions extends P100

}
