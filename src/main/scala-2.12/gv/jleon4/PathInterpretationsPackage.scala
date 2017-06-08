package gv
package jleon4

import java.nio.file.{ Path ⇒ JPath }

object PathInterpretationsPackage extends PathPackage with TypeClassPackage {
  implicit class JPathPath(val self: JPath) extends AnyVal with Path.Ops {
    final type Self = JPath
  }
}
