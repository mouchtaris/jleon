package gv
package jleon4

import language.{ implicitConversions }

import isi.io.{ File }

trait StoragePackage {
  // format: OFF
  this: Any
    with Util
    with TypeClassPackage
    with StorageMapPackage
  ⇒
  // format: ON

  sealed trait LockResult
  final object LockResult {
    case class Acquired(channel: WritableByteChannel) extends LockResult
    case class Found(channel: ReadableByteChannel) extends LockResult

    // Errors

    case class Locked(item: String, cause: FileAlreadyExistsException)
      extends Exception(s"item is locked: $item", cause)

    case class Failed(item: String)
      extends Exception(s"item is failed: $item")
  }

  sealed trait UnlockResult
  final object UnlockResult {
    case object Unlocked extends UnlockResult

    // Errors
    case class NotLocked(item: String) extends Exception(s"item not locked: $item")
  }

  trait Storage[-T] extends TypeClass.WithTypeParams[T, Storage.Ops]

  object Storage extends TypeClassCompanion[Storage] {

    val nothing: Try[LockResult] = Failure(new Exception("nothing happened yet"))

    trait Ops {
      type StorageMap <: StoragePackage.this.StorageMap.Ops
      def storageMap: StorageMap

      final class ForItem(item: String) {
        val map = storageMap(item)

        val getFailure: Try[LockResult] =
          if (File exists map.failure)
            Failure(LockResult.Failed(map.item))
          else
            Failure(new NoSuchFileException(map.failure.toString))

        val getLock: Try[LockResult] = Try {
          File create map.lock close ()
        } map { _ ⇒
          File create map.storage
        } map {
          LockResult.Acquired.apply
        } recoverWith {
          case ex: FileAlreadyExistsException ⇒ Failure(LockResult.Locked(map.item, ex))
        }

        val getStorage: Try[LockResult] = Try {
          File open map.storage
        } map {
          LockResult.Found.apply
        }

        val tryLock: Try[LockResult] =
          nothing recoverWith pf(getFailure) recoverWith pf(getLock) recoverWith pf(getStorage)

        val tryUnlock: Try[UnlockResult] =
          if (File exists map.lock)
            Try { File remove map.lock } map (_ ⇒ UnlockResult.Unlocked)
        else
            Failure(UnlockResult.NotLocked(map.item))
      }

      final def apply(item: String): ForItem = new ForItem(item)

      final val tryLock: String ⇒ Try[LockResult] = apply _ andThen (_.tryLock)

      final val tryUnlock: String ⇒ Try[UnlockResult] = apply _ andThen (_.tryUnlock)
    }
  }

  implicit def storageOps[T: Storage](self: T): Storage.Ops = Storage[T](self)

}
