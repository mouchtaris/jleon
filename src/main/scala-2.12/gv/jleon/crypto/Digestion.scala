package gv.jleon
package crypto

import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

import scala.language.{ implicitConversions }

import `type`.{ TaggedType }

import Digestion._

final case class Digestion(
    digest: DigestFunc.t
) {
  import Digestion.{ toHexString }

  def digest(string: String): Bytes = digest(string getBytes UTF_8)

  def hexDigest(bytes: Bytes): String = toHexString(digest(bytes))

  def hexDigest(string: String): String = toHexString(digest(string))
}

object Digestion {

  type Bytes = Array[Byte]
  final implicit object DigestFunc extends TaggedType[Bytes ⇒ Bytes]

  def toHexString(bytes: Bytes): String =
    BigInt(1, bytes).toString(0x10)

  trait Interpretation[T] extends Any {
    def digest(self: T): DigestFunc.t
  }

  final implicit def apply[T: Interpretation](self: T): Digestion = {
    val i: Interpretation[T] = implicitly
    Digestion(
      digest = i digest self
    )
  }

  final implicit object MessageDigestConstructorInterpretation extends Interpretation[() ⇒ MessageDigest] {
    override def digest(self: () ⇒ MessageDigest): DigestFunc.t = self().digest(_)
  }

  final val SHA512: Digestion = Digestion(() ⇒ MessageDigest getInstance "SHA-512")

}
