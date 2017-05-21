package gv

import jleon.util.{ stdconv, stdimport }
import jleon.facade.{ JLeonImports }

package object jleon extends AnyRef
    with stdconv
    with stdimport
    with JLeonImports {
  protected[jleon] final implicit class FetchRepository(val self: Map[String, Fetch]) extends AnyVal {
    @inline def apply(s: String): Option[Fetch] = self get s
  }

}
