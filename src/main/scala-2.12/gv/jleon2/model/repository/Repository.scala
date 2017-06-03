package gv
package jleon2
package model.repository

import concurrent.{ Future }

trait Repository extends Any {

  // Inputs
  type Key

  // Output
  type Value

  def apply(key: Key): Future[Value]

}
