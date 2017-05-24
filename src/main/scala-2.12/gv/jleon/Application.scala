package gv.jleon

object Application {

  def main(args: Array[String]): Unit = {
    val jleon = JLeon()
    import jleon.materializer

    implicit val prefix: Mirror.Prefix = "local"
    val u = Uri("/build.sbtea")

    import jleon.actorSystem.dispatcher

    val source = (jleon.fetchManager.fetch(0, _: Uri)) andThen Future.fromTry andThen Source.fromFuture apply u
    source
      .flatMapMerge(12, Predef.identity)
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
