package gv.jleon
package storage

final case class create(
  basePath: Path
)

object create {

  final implicit case object Interpretation extends storage.Interpretation[create] {
    override def basePath(self: create): Path = self.basePath
  }

}
