package gv.jleon

package object domain extends AnyRef
    with FetchRepositoryImports {

  final implicit class LockingStorage(override val self: Storage) extends AnyVal with LockingStorageOps

}
