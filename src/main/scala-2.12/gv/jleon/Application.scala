package gv.jleon

import gv.jleon._

object Application {

  def main(args: Array[String]): Unit = {
    import jleon.materializer

    val jleon = JLeon()
    val u = Uri("/build.sbt")
    implicit val prefix: Mirror.Prefix = "local"
    jleon.fetchManager.fetch(0, u)
      .map { source ⇒
        source.runForeach { bytestr ⇒
          println(bytestr.decodeString(java.nio.charset.StandardCharsets.UTF_8))
        }
      }
      .recover {
        case ex ⇒ println(s" failt: $ex")
      }

    scala.io.Source.stdin.bufferedReader().readLine()
    jleon.actorSystem.terminate()
  }

}
