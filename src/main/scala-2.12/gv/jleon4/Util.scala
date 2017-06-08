package gv
package jleon4

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

object Util extends Util {

  final case object CouldBeSingleton

  implicit class CouldBe[T](val self: CouldBeSingleton.type) extends AnyVal {
    final type t[a] = a ⇒ T

    def apply[U](u: U)(implicit ev: U ⇒ T): U = u
  }

}

trait Util {
  final def pf[T](t: ⇒ T): Any ~> T = { case _ ⇒ t }

  /** syntax-require: block is never run */
  final def squire(t: ⇒ Any): Unit = ()

  final def couldBe[T]: Util.CouldBe[T] = Util.CouldBeSingleton
}
