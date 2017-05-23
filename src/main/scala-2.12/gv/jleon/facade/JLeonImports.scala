package gv.jleon
package facade

trait JLeonImports {

  type Mirror = domain.Mirror
  val Mirror = domain.Mirror

  type Storage = domain.Storage
  val Storage = domain.Storage

  type FetchManager = domain.FetchManager
  val FetchManager = domain.FetchManager

  type MirrorRepository = domain.MirrorRepository
  type FetchRepository = domain.FetchRepository
  type LockingStorage = domain.LockingStorage

  type JLeon = facade.JLeon
  val JLeon = facade.JLeon

}
