package gv.jleon

import gv.jleon.facade.JLeon

object Application {

  def main(args: Array[String]): Unit = {
    val jleon = JLeon()
    jleon.actorSystem.terminate()
    println {
      jleon.mirrors
    }
  }

}
