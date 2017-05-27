package gv.isi.functional

object TupledCall {

  final implicit class Decoration[I, S](val interpSelf: (I, S)) extends AnyVal {
    def getf[R](getf: I ⇒ S ⇒ R): R = interpSelf match {
      case (interp, self) ⇒ getf(interp)(self)
    }
  }
}
