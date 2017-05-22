package gv.jleon

package object domain extends AnyRef {
  final implicit class FetchRepository(override val self: FetchRepositories.Underlying) extends AnyVal with FetchRepositories.Ops
  final implicit class MirrorRepository(override val self: MirrorRepositories.Underlying) extends AnyVal with MirrorRepositories.Ops
  final implicit class LockingStorage(override val self: LockingStorages.Underlying) extends AnyVal with LockingStorages.Ops
}
