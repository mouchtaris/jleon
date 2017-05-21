package gv.jleon

object Application {

  def main(args: Array[String]): Unit = {
    val jleon = JLeon()
    jleon.actorSystem.terminate()
    println {
      jleon.mirrors
    }
  }

}
