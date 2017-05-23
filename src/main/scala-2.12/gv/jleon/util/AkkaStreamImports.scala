package gv.jleon
package util

import akka.stream.{ scaladsl ⇒ akkas }

trait AkkaStreamImports {
  final type NotUsed = akka.NotUsed
  final val NotUsed = akka.NotUsed

  final type Source[Out, Mat] = akkas.Source[Out, Mat]
  final type Source1[Out] = Source[Out, NotUsed]
  final val Source = akkas.Source

  final type Sink[In, Mat] = akkas.Sink[In, Mat]
  final type Sink1[In] = Sink[In, NotUsed]
  final val Sink = akkas.Sink

  final type Flow[In, Out, Mat] = akkas.Flow[In, Out, Mat]
  final type Flow1[In, Out] = Flow[In, Out, NotUsed]
  final val Flow = akkas.Flow

  final type ByteString = akka.util.ByteString
  final val ByteString = akka.util.ByteString
}
