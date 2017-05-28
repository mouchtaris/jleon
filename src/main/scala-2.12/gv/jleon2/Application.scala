package gv
package jleon2

import scala.concurrent.{ Future, ExecutionContext, Await, duration }

import gv.{ jleon2 ⇒ leon }

object Application {

  import akka.http.scaladsl.model.{ Uri }

  class Storage extends leon.model.storage.Storage {
    import leon.model.storage.LockResult

    override type Request = Uri

    override def tryLock(request: Uri): Future[LockResult] = Future successful {
      LockResult Failed new RuntimeException(s"no locking yet: $request")
    }
  }

  class Mirror extends leon.model.mirror.Mirror

  class MirrorRepository extends leon.model.mirror.MirrorRepository {
    override type Prefix = String
    override type Mirror = Application.Mirror

    override def apply(prefix: Prefix): Future[Mirror] = prefix match {
      case "bohos" ⇒ Future successful new Mirror
      case _       ⇒ Future failed new NoSuchElementException(s"prefix poo: $prefix")
    }
  }

  class JLeon extends leon.model.JLeon {
    import java.util.concurrent.Executors.{ newWorkStealingPool }
    import isi.std.conversions.ExecutionContextConversions._

    override type Storage = Application.Storage
    override object Storage extends Storage

    override type MirrorRepository = Application.MirrorRepository
    override object MirrorRepository extends MirrorRepository

    override object ExecutionContexts extends leon.model.facade.ExecutionContexts {
      override val RequestProcessing: ExecutionContext = newWorkStealingPool(16)
    }
  }

  def main(args: Array[String]): Unit = {

    val leon = new JLeon

    Await.ready(
      leon.serveRequest("bohos", Uri("/bohos/me/a/las/rajas")),
      duration.Duration(3, duration.SECONDS)
    )

  }

}
