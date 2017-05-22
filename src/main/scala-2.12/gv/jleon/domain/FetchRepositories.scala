package gv.jleon
package domain

object FetchRepositories {

  final type Underlying = Map[String, Fetch]

  trait Ops extends Any {
    def self: Underlying

    @inline def apply(s: String): Option[Fetch] = self get s
  }

}
