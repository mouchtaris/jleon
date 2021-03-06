package gv.jleon
package test

import java.net.URI

import Uri.{ Path ⇒ AkkaPath }

import `type`.TaggedType

object UriGenerator {

  final implicit object Scheme extends TaggedType[String]
  type Scheme = Scheme.t
  final implicit object Host extends TaggedType[String]
  type Host = Host.t
  final implicit object Ext extends TaggedType[String]
  type Ext = Ext.t
  final implicit object UriString extends TaggedType[String]
  type UriString = UriString.t

}

import UriGenerator._

trait UriGenerator extends Any {

  private[this] final implicit def schemeGenerator: Arbitrary[Scheme] = Arbitrary(
    Gen
      .oneOf("http", "https", "ftp", "ftps", "scp", "ssh", "git")
      .withFilter(_.nonEmpty)
      .map(Scheme.apply)
  )

  private[this] final implicit def hostGenerator: Arbitrary[Host] = Arbitrary(
    Gen.alphaStr.map(Host.apply)
  )

  private[this] final implicit def extGenerator: Arbitrary[Ext] = Arbitrary(
    Gen.alphaStr.map(Ext.apply)
  )

  private[this] final implicit def pathSegmentGenerator: Arbitrary[AkkaPath.Slash] = Arbitrary(
    Gen.alphaStr.withFilter(_.nonEmpty).map(s ⇒ AkkaPath.Slash(s :: AkkaPath.Empty))
  )

  final implicit def akkaUriPathGenerator: Arbitrary[AkkaPath] = Arbitrary(
    Gen.listOf(pathSegmentGenerator.arbitrary).map(_.fold(AkkaPath./)(_ ++ _))
  )

  private[this] final implicit def uriStringGenerator: Arbitrary[UriString] = Arbitrary(
    for {
      scheme ← arbitrary[Scheme]
      host ← arbitrary[Host]
      ext ← arbitrary[Ext]
      path ← arbitrary[AkkaPath]
    } yield s"$scheme://$host.$ext$path"
  )

  final implicit def javaUriGenerator: Arbitrary[URI] = Arbitrary(
    for (str ← arbitrary[UriString]) yield URI.create(str)
  )

  final implicit def akkaUriGenerator: Arbitrary[Uri] = Arbitrary(
    for (str ← arbitrary[UriString]) yield Uri(str)
  )

}
