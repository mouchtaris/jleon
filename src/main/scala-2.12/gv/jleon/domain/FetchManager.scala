package gv.jleon
package domain

final case class FetchManager(
  mirrors: MirrorRepository,
  storage: LockingStorage
)

object FetchManager {

}
