package gv
package isi
package concurrent

import java.util.concurrent.{ Executor, Executors }

trait Executors extends AnyRef {

  final case object SyncExecutor extends Executor {
    def execute(command: Runnable): Unit = command.run()
  }

}

object Executors extends Executors
