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
import isi.{ ~~> }

import gv.{ jleon4 ⇒ app }

import Util._

trait TypeClassPackage {

  trait TypeClass[-Self] extends Any {
    type Out

    def apply(self: Self): Out
  }

  object TypeClass {
    trait WithTypeParams[-Self, out] extends Any with TypeClass[Self] {
      final type Out = out
    }
  }

  trait TypeClassCompanion[TC[T] <: TypeClass[T]] {
    def apply[T: TC]: TC[T] = implicitly
    def apply[T](self: T)(implicit tc: TC[T]): tc.Out = tc(self)
  }

}
