package gv.jleon
package util

import akka.http.{ scaladsl ⇒ akkah }

trait AkkaHttpImports {

  final type StatusCode = akkah.model.StatusCode
  final val StatusCode = akkah.model.StatusCode
  final val StatusCodes = akkah.model.StatusCodes

  final val RequestBuilding = akkah.client.RequestBuilding

  final type HttpExt = akkah.HttpExt
}
