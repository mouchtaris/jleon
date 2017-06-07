package gv.isi
package std
package conversions

import scala.language.{ implicitConversions }

import java.{ net ⇒ jnet }
import java.{ nio ⇒ jnio }

trait PathConversions extends Any {

  final implicit def `URI ⇒ Path`: jnet.URI ⇒ jnio.file.Path = jnio.file.Paths.get

}
