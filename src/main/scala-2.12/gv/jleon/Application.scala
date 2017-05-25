package gv.jleon

object Application {

  def main(args: Array[String]): Unit = {
    val jleon = JLeon()
    import jleon.materializer

    implicit val prefix: Mirror.Prefix = "local"
    val u = Uri("/build.sbtea")

    import jleon.actorSystem.dispatcher

    jleon.fetchManager.fetch(0, u)
      .map(_ decodeString UTF_8)
      .runForeach(println)
      .andThen {
        case Success(Done) ⇒ println("*** Fetching ok")
        case Failure(ex)   ⇒ println(s"fiald: $ex")
      }

    scala.io.Source.stdin.bufferedReader().readLine()
    jleon.actorSystem.terminate()
  }

}
