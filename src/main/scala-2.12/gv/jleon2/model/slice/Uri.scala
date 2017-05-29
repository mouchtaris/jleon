package gv.jleon2.model.slice

object Uri {
  trait Types extends Any {
    type Uri
  }
}

trait Uri extends Any with Uri.Types
