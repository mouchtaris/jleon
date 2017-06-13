package gv
package jleon4

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

    case class Locked(item: String, cause: FileAlreadyExistsException)
      extends Exception(s"item is locked: $item", cause) with LockResult

    case class Failed(item: String)
      extends Exception(s"item is failed: $item") with LockResult
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
            Success(LockResult.Failed(map.item))
          else
            Failure(new NoSuchFileException(map.failure.toString))

        val getLock: Try[LockResult] = Try {
          File create map.lock close ()
        } map { _ ⇒
          File create map.storage
        } map {
          LockResult.Acquired.apply
        } recover {
          case ex: FileAlreadyExistsException ⇒ LockResult.Locked(map.item, ex)
        }

        val getStorage: Try[LockResult] = Try {
          File open map.storage
        } map {
          LockResult.Found.apply
        }

        val tryLock: Try[LockResult] =
          nothing recoverWith pf(getFailure) recoverWith pf(getLock) recoverWith pf(getStorage)
      }

      final def apply(item: String): ForItem = new ForItem(item)

      final val tryLock: String ⇒ Try[LockResult] = apply _ andThen (_.tryLock)
    }
  }

  implicit def storageOps[T: Storage](self: T): Storage.Ops = Storage[T](self)

}
