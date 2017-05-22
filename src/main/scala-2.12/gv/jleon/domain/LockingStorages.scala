package gv.jleon
package domain

object LockingStorages {
  outer0 ⇒

  final type Underlying = Storage

  trait Ops extends Any {

    def self: Underlying

    final def lockPath(uri: Uri): Path = {
      val storagePath = self.storagePath(uri)
      val name = storagePath.getFileName
      val lockName = s"$name.lock"
      storagePath resolveSibling lockName
    }

    final def lock(uri: Uri): Try[ReadableByteChannel] =
      File.createNew(lockPath(uri))

    final def unlock(uri: Uri): Try[Unit] =
      if (File.delete(lockPath(uri)))
        Success {}
      else
        Failure {
          new java.io.FileNotFoundException(s"$uri - ${lockPath(uri)}")
        }
  }

}
