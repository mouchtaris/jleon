package gv.isi
package std
package conversions

import java.{ net ⇒ jnet }
import java.{ nio ⇒ jnio }

trait PathConversions {

  final implicit val `URI ⇒ Path`: jnet.URI ⇒ jnio.file.Path = jnio.file.Paths.get

}
