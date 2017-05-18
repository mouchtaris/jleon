package gv.jleon
package storage

trait Interpretation[T] extends Any {
  def basePath(self: T): Path
}
