package gv
package jleon4

import language.{ postfixOps, implicitConversions }

trait ConfigPackage {
  this: TypeClassPackage ⇒

  trait Config[-T] extends Any with TypeClass.WithTypeParams[T, Config.Ops]

  object Config extends TypeClassCompanion[Config] {
    trait Ops extends Any {
      type Config <: TSConfig
      type FileSystem <: JFileSystem
      def config: Config
      def fileSystem: FileSystem

      final def getPath: String ⇒ JPath = (config getString _) andThen (fileSystem getPath _)
      final def getUri: String ⇒ JUri = (config getString _) andThen (java.net.URI create)
    }

    object Ops {
      implicit def toTSConfig(ops: Ops): TSConfig = ops.config
    }
  }

  implicit def configOps[T: Config](self: T): Config.Ops = Config[T](self)
}
