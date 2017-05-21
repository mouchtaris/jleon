package gv.jleon
package domain

object FetchRepositories {

  final implicit class FetchRepository(val self: Map[String, Fetch]) extends AnyVal {
    @inline def apply(s: String): Option[Fetch] = self get s
  }

}
