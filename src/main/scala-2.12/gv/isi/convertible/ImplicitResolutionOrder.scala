package gv.isi
package convertible

/**
 * Provide priority ordering for resolving generic conversions last.
 */
object ImplicitResolutionOrder {

  trait P25 extends ParentalConversions
  trait P50 extends P25 with ChainedConversions
  trait P100 extends P50

  trait Conversions extends P100

}
