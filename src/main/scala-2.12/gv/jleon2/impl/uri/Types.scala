package gv
package jleon2
package impl.uri

trait Types extends Any with model.slice.Uri.Types {
  final type Uri = ThinUri
}

object Types extends Types
