package gv
package jleon2
package model.mirror

trait RepositoryFactory extends Any {
  type Config
  type Repository <: model.mirror.Repository

  def apply(config: Config): Repository
}
