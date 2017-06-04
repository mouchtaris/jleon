package gv
package jleon2
package model.mirror

trait RepositoryFactory extends Any {
  type Source

  type Repository <: model.mirror.Repository

  def apply(source: Source): Repository
}
