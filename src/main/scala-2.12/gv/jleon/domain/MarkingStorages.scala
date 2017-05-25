package gv.jleon
package domain

object MarkingStorages {
  outer0 ⇒

  final type Underlying = Storage

  trait Ops extends Any {
    def self: Underlying

    final def failurePath(uri: Uri): Storage.Path =
      self storagePathWithExt (uri, "failed")

    final def isFailed(uri: Uri): Boolean =
      File exists failurePath(uri)

    final def markFailed(uri: Uri): Try[Unit] =
      File.createNew(failurePath(uri))
        .map { _.close() }

  }
}
