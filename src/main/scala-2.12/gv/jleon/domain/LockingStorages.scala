package gv.jleon
package domain

import java.io.{ FileNotFoundException }
import java.nio.file.{ FileAlreadyExistsException }

object LockingStorages {

  final type Underlying = Storage

  final case class Locked(uri: Uri, cause: Throwable) extends Exception(s"locked: $uri", cause)

  trait Ops extends Any {

    def self: Underlying

    final def lockPath(uri: Uri): Storage.Path =
      self storagePathWithExt (uri, "lock")

    final def lock(uri: Uri): Try[WritableByteChannel] = {
      File.createNew(lockPath(uri))
        .recoverWith {
          case cause: FileAlreadyExistsException ⇒
            Failure(Locked(uri, cause))
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
