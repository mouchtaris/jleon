package gv.jleon.functional.monads

trait ScalaGenericCompanions {

  final implicit val Vector = collection.immutable.Vector
  final implicit val List = collection.immutable.List
  final implicit val Seq = collection.immutable.Seq
  final implicit val Set = collection.immutable.Set
  final implicit val Map = collection.immutable.Map

}
