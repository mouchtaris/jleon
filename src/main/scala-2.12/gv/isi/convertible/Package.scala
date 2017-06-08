package gv
package isi.convertible

trait Package extends Any {

  final type ~=>[A, B] = Conversion[A, B]

  final type ~⇒[A, B] = A ~=> B

}
