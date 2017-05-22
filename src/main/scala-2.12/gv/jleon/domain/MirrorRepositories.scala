package gv.jleon
package domain

import shapeless.{ ::, HNil }

object MirrorRepositories {
  outer0 ⇒

  import Mirror.{ Prefix }

  final type Underlying = Map[Prefix, Traversable[Mirror :: Fetch :: HNil]]

  trait Ops extends Any {
    def self: Underlying
  }

}
