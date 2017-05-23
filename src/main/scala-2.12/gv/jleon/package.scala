package gv

import jleon.util.{ stdconv, stdimport, AkkaStreamImports }
import jleon.facade.{ JLeonImports }

package object jleon extends AnyRef
    with stdconv
    with stdimport
    with AkkaStreamImports
    with JLeonImports {

  def apply(): JLeon = JLeon()
}
