package gv
package jleon4

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

object Main extends StrictLogging {

  object Util {
    def pf[T](t: ⇒ T): Any ~> T = { case _ ⇒ t }
  }

  trait Path[T <: JPath] extends Any {
    final type Self = T
    def apply(self: Self): Path.Ops.Aux[T]
  }

  object Path {
    trait Ops extends Any {
      type Self <: JPath
      def self: Self
      def addExt(ext: String): JPath = self resolveSibling s"${self.getFileName}.$ext"
    }

    object Ops {
      type Aux[T] = Ops { type Self = T }
    }

    def apply[T <: JPath : Path]: Path[T] = implicitly
    implicit def fromConstructor[T <: JPath : Path.Ops.Aux]: Path[T] = self ⇒ implicitly
  }

  implicit class JPathPath(val self: JPath) extends AnyVal with Path.Ops {
    final type Self = JPath
  }
  implicit val JPathPathConstructor: Path[JPath] = implicitly

  trait StorageMap[T] extends Any {
    final type Self = T
    def apply(self: Self): {
      def forItem(item: String): {
        val storage: JPath
        val lock: JPath
        val failure: JPath
      }
    }
  }

//  implicit def StorageMapWithPath[P: Path]: StorageMap[P :: HList] = { case _base :: _ ⇒ new {
//    private[this] val base = Path[P].apply(_base)
//    def forItem(item: String) = new {
//      val storage: JPath = base.asJPath resolveSibling item
//      val lock: JPath = storage
//    }
//  }}
//  final case class StorageMap(base: JPath) {
//    final case class forItem(item: String) {
//      val storage: JPath = base resolveSibling item
//      val lock: JPath = storage addExt StorageMap.EXT_LOCK
//      val failure: JPath = storage addExt StorageMap.EXT_FAILURE
//    }
//  }
//
  object StorageMap {
    final val EXT_LOCK = "lock"
    final val EXT_FAILURE = "failed"
  }

  def main(args: Array[String]): Unit = {
    println("hello")
  }

}
