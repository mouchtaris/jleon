package gv
package isi
package io

import java.nio.channels.{ WritableByteChannel, ReadableByteChannel }
import java.nio.file.{ StandardOpenOption ⇒ opt, Files ⇒ JFiles, Path ⇒ JPath }

trait File extends Any {

  @inline
  final def exists(path: JPath): Boolean =
    JFiles exists path

  @inline
  final def create(path: JPath): WritableByteChannel =
    JFiles newByteChannel (path, opt.CREATE_NEW, opt.WRITE)

  @inline
  final def open(path: JPath): ReadableByteChannel =
    JFiles newByteChannel (path, opt.CREATE, opt.READ)

  @inline
  final def append(path: JPath): WritableByteChannel =
    JFiles newByteChannel (path, opt.CREATE, opt.WRITE, opt.APPEND)

  @inline
  final def truncate(path: JPath): WritableByteChannel =
    JFiles newByteChannel (path, opt.CREATE, opt.WRITE, opt.TRUNCATE_EXISTING)

  @inline
  final def remove(path: JPath): Unit =
    JFiles delete path
}

object File extends File
