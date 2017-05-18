package gv.jleon
package test

import java.net.URI

import scala.annotation.tailrec

import akka.http.scaladsl.model.Uri.Path

import `type`.{ TypeWrap }

object UriGenerator {

  final implicit class Scheme(override val v: String) extends AnyVal with TypeWrap[String]
  final implicit class Host(override val v: String) extends AnyVal with TypeWrap[String]
  final implicit class Ext(override val v: String) extends AnyVal with TypeWrap[String]
  final implicit class UriString(override val v: String) extends AnyVal with TypeWrap[String]

}

import UriGenerator._

trait UriGenerator {

  final implicit def schemeGenerator: Gen[Scheme] =
    Gen
      .oneOf("http", "https", "ftp", "ftps", "scp", "ssh", "git")
      .withFilter(_.nonEmpty)
      .map(Scheme)

  final implicit def hostGenerator: Gen[Host] =
    Gen.alphaStr.map(Host)

  final implicit def extGenerator: Gen[Ext] =
    Gen.alphaStr.map(Ext)

  final implicit def pathSegmentGenerator: Gen[Path] =
    Gen.alphaStr.withFilter(_.nonEmpty).map(Path / _)

  final implicit def pathGenerator: Gen[Path] =
    Gen.listOf(pathSegmentGenerator).map(_.fold(Path./)(_ ++ _))

  final implicit def uriStringGenerator: Gen[UriString] =
    for {
      scheme ← schemeGenerator
      host ← hostGenerator
      ext ← extGenerator
      path ← pathGenerator
    } yield s"$scheme://$host.$ext$path"

  final implicit def javaUriGenerator: Gen[URI] =
    for (str ← uriStringGenerator) yield URI.create(str)

  final implicit def akkaUriGenerator: Gen[Uri] =
    for (str ← uriStringGenerator) yield Uri(str)

}
