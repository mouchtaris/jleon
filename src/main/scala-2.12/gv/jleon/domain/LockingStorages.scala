package gv.jleon
package domain

import java.io.{ FileNotFoundException }
import java.nio.file.{ FileAlreadyExistsException }

object LockingStorages {

  final type Underlying = Storage

  trait Ops extends Any {

    def self: Underlying

    final def lockPath(uri: Uri): Storage.Path =
      self storagePathWithExt (uri, "lock")

    final def lock(uri: Uri): Try[ReadableByteChannel] = {
      File.createNew(lockPath(uri))
        .recoverWith {
          case _: FileAlreadyExistsException ⇒
            Failure(new IllegalStateException(s"Path already locked: $uri"))
        }
    }

    final def isLocked(uri: Uri): Boolean =
      File exists lockPath(uri)

    final def unlock(uri: Uri): Try[Unit] =
      if (File.delete(lockPath(uri)))
        Success {}
      else
        Failure {
          new FileNotFoundException(s"$uri - ${lockPath(uri)}")
        }
  }

}
