package gv.jleon
package domain

trait LockingStorageOps extends Any {

  def self: Storage

  final def lockPath(uri: Uri): Path = {
    val storagePath = self.storagePath(uri)
    val name = storagePath.getFileName
    val lockName = s"$name.lock"
    storagePath resolveSibling lockName
  }

  final def lock(uri: Uri): Try[ReadableByteChannel] =
    File.createNew(lockPath(uri))

}
