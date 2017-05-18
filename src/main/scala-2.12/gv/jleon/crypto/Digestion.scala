package gv.jleon.crypto

import java.nio.charset.StandardCharsets.{ UTF_8 }
import java.security.{ MessageDigest }

import scala.language.{ implicitConversions }

trait Digestion extends Any {
  import Digestion.{ toHexString }

  def digest(bytes: Array[Byte]): Array[Byte]

  def digest(string: String): Array[Byte] = digest(string getBytes UTF_8)

  def hexDigest(bytes: Array[Byte]): String = toHexString(digest(bytes))

  def hexDigest(string: String): String = toHexString(digest(string))
}

object Digestion {

  def toHexString(bytes: Array[Byte]): String =
    BigInt(1, bytes).toString(0x10)

  final case class create(
      create: () ⇒ MessageDigest
  ) extends Digestion {
    override def digest(bytes: Array[Byte]): Array[Byte] = create().digest(bytes)
  }

  final val SHA512: Digestion =
    create(() ⇒ MessageDigest getInstance "SHA-512")

}
