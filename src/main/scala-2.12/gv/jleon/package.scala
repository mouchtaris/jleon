package gv

import jleon.util.{ stdconv, stdimport }

package object jleon extends AnyRef
    with stdconv
    with stdimport {
  protected[jleon] final implicit class FetchRepository(val self: Map[String, Fetch]) extends AnyVal {
    @inline def apply(s: String): Option[Fetch] = self get s
  }

}
