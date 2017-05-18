package gv.jleon

import akka.actor.{ ActorSystem }
import com.typesafe.config.{ ConfigFactory }

object JLeon {

  implicit val actorSystem: ActorSystem = ActorSystem("JLeon")

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.defaultApplication()
    println { conf.getStringList("leon.mirrors.arch") }
    //    Http.serve
  }

}
