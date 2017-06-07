package gv
package jleon2
package model.http

import concurrent.{ Future }

trait Client extends AnyRef with Client.Types {

  val get: Uri ⇒ Future[Response]

}


object Client {
  trait Types extends Any {
    type Uri <: model.uri.Uri
    type Response <: model.http.Response
  }
}
