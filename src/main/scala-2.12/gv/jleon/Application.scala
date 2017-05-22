package gv.jleon

import gv.jleon.facade.JLeon

object Application {

  def main(args: Array[String]): Unit = {
    val jleon = JLeon()
    jleon.actorSystem.terminate()
    println {
      jleon.mirrors
    }
    println {
      jleon.storage
    }
    val u = Uri("file:///tmp/a")
    println {
      jleon.storage.lock(u)
        .recoverWith {
          case _ ⇒ jleon.storage.unlock(u) flatMap (_ ⇒ jleon.storage.lock(u))
        }
    }
  }

}
