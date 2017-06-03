package gv
package jleon2
package impl.mirror

trait Types extends Any
  with model.slice.Mirror.Types
  with impl.uri.Types {
  type Factory = impl.mirror.RepositoryFactory
  final type Mirror = impl.mirror.Repository
}
