package gv.jleon4

trait Imports {
  final type JUri = java.net.URI

  final type JPath = java.nio.file.Path
  final type JFileSystem = java.nio.file.FileSystem
  final type FileAlreadyExistsException = java.nio.file.FileAlreadyExistsException
  final type NoSuchFileException = java.nio.file.NoSuchFileException

  final type ReadableByteChannel = java.nio.channels.ReadableByteChannel
  final type WritableByteChannel = java.nio.channels.WritableByteChannel
  final type Channels = java.nio.channels.Channels

  final type Try[T] = util.Try[T]
  final val Try = util.Try
  final type Success[T] = util.Success[T]
  final val Success = util.Success
  final type Failure[T] = util.Failure[T]
  final val Failure = util.Failure

  final type Future[T] = concurrent.Future[T]
  final val Future = concurrent.Future
  final type ExecutionContext = concurrent.ExecutionContext
  final val ExecutionContext = concurrent.ExecutionContext
  final val Await = concurrent.Await

  import akka.stream.{ scaladsl ⇒ stream }
  final type NotUsed = akka.NotUsed
  final val NotUsed = akka.NotUsed
  final type Done = akka.Done
  final val Done = akka.Done
  final type Source[Out, Mat] = stream.Source[Out, Mat]
  final type SourceWithMat[Mat] = { type t[Out] = stream.Source[Out, Mat] }
  final val Source = stream.Source
  final type Flow[In, Out, Mat] = stream.Flow[In, Out, Mat]
  final val Flow = stream.Flow
  final type Sink[In, Mat] = stream.Sink[In, Mat]
  final val Sink = stream.Sink
  final val Keep = stream.Keep

  final type Source1[Out] = Source[Out, NotUsed]
  final type Flow2[In, Out] = Flow[In, Out, NotUsed]
  final type Sink1[In] = Sink[In, Future[Done]]

  final type Materializer = akka.stream.Materializer
  final type ActorMaterializer = akka.stream.ActorMaterializer

  final type ActorSystem = akka.actor.ActorSystem

  import akka.http.{ scaladsl ⇒ http }

  final val Http = http.Http

  final type Route = http.server.Route
  final val Directives = http.server.Directives
  final type Directive0 = http.server.Directive0
  final type Directive1[T] = http.server.Directive1[T]

  final type Uri = http.model.Uri
  final val Uri = http.model.Uri

  import com.typesafe.{ config ⇒ tsconfig }
  final type TSConfig = tsconfig.Config
  final type TSConfigFactory = tsconfig.ConfigFactory
  final case object TSConfigFactory {
    import tsconfig.{ ConfigFactory ⇒ fac }
    @inline def defaultApplication(): TSConfig = fac.defaultApplication()
  }

  import com.typesafe.{ scalalogging ⇒ tslogging }
  final type StrictLogging = tslogging.StrictLogging
  final type Logger = tslogging.Logger

  final type ByteString = akka.util.ByteString
  final val ByteString = akka.util.ByteString

  final type HList = shapeless.HList
  final type HNil = shapeless.HNil
  final val HNil = shapeless.HNil
  final type ::[H, T <: HList] = shapeless.::[H, T]
  final val :: = shapeless.::
}
