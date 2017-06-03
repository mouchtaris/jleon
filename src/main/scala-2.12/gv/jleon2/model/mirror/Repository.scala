package gv
package jleon2
package model.mirror

trait Repository extends Any with model.repository.Repository {
  type Handler <: model.mirror.Handler
  type Prefix

  final type Value = Handler
  final type Key = Prefix
}
