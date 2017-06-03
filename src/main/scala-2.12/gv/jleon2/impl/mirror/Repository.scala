package gv
package jleon2
package impl.mirror

import concurrent.{ Future }

final case class Repository(
  handlers: Map[RepositoryFactory#Repository#Prefix, RepositoryFactory#Repository#Handler]
) extends AnyRef
  with model.mirror.Repository {

  type Prefix = String
  type Handler = impl.mirror.Handler

  def apply(key: Key): Future[Value] = {
    import isi.convertible._
    import isi.std.conversions.ToTryConversions
    object all extends ToTryConversions
    import all.{ fromOption }
    handlers.get(key).convertToEffect[util.Try]
    ???
  }
}
