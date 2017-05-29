package gv
package isi
package std.conversions

object ImplicitResolutionOrder {

  trait P0 extends AnyRef
  //    with convertible.ImplicitResolutionOrder.Conversions
  trait P50 extends AnyRef with P0
    with ByteConversions
  trait P60 extends AnyRef with P50
    with JavaIoConversions
  trait P70 extends AnyRef with P60
    with ToFutureConversions
    with UriConversions
  trait P80 extends AnyRef with P70
    with ExecutionContextConversions
    with ToTryConversions
  trait P90 extends AnyRef with P80
  trait P100 extends AnyRef with P90

  trait Conversions extends AnyRef with P100
}
