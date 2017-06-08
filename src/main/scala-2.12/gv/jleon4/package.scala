package gv

import java.net.{ URI ⇒ JUri }

import java.nio.file.{ Path ⇒ JPath, FileSystem ⇒ JFileSystem, FileAlreadyExistsException, NoSuchFileException }
import java.nio.channels.{ ReadableByteChannel, WritableByteChannel, Channels }

import language.{ postfixOps, implicitConversions, higherKinds, existentials }
import util.{ Try, Success, Failure }
import concurrent.{ Future, ExecutionContext, Await }
import concurrent.duration._

import akka.stream.scaladsl.{ Source, Flow, Sink }
import akka.stream.{ Materializer, ActorMaterializer }
import akka.actor.{ ActorSystem }
import akka.http.scaladsl.server.{ Route, Directives, Directive0 }
import akka.http.scaladsl.{ Http }

import com.typesafe.config.{ Config ⇒ TSConfig, ConfigFactory ⇒ TSConfigFactory }
import com.typesafe.scalalogging.{ StrictLogging, Logger }

import shapeless.{ HNil, ::, HList }

import isi.convertible._
import isi.std.conversions._
import isi.{ ~> }

import gv.{ jleon4 ⇒ app }

package object jleon4 {

  final type JUri = java.net.URI

  final type JPath = java.nio.file.Path
  final type FileSystem = java.nio.file.FileSystem
  final type FileAlreadyExistsException = java.nio.file.FileAlreadyExistsException
  final type NoSuchFileException = java.nio.file.NoSuchFileException

  final type ReadableByteChannel = java.nio.channels.ReadableByteChannel
  final type WritableByteChannel = java.nio.channels.WritableByteChannel
  final type Channels = java.nio.channels.Channels

  final type Try[T] = util.Try[T]
  final type Success[T] = util.Success[T]
  final type Failure[T] = util.Failure[T]

  final type Future[T] = concurrent.Future[T]
  final type ExecutionContext = concurrent.ExecutionContext
  final val Await = concurrent.Await

}
