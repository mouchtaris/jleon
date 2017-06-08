package gv
package jleon4

trait PathPackage {
  this: TypeClassPackage ⇒

  trait Path[T] extends Any with TypeClass.WithTypeParams[T, Path.Ops]

  object Path extends TypeClassCompanion[Path] {

    trait Ops extends Any {
      type Self <: JPath
      def self: Self

      final def addExt(ext: String): JPath = self resolveSibling s"${self.getFileName}.$ext"
    }

    final object Ops
  }

}
