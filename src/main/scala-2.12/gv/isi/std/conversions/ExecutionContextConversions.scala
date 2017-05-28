package gv.isi
package std
package conversions

import scala.language.{ implicitConversions }

trait ExecutionContextConversions extends Any {

  import scala.concurrent.{ ExecutionContext }
  import java.util.concurrent.{ Executor, ExecutorService }

  @inline
  final implicit def javaExecutorServiceToScalaExecutionContext(service: ExecutorService): ExecutionContext =
    ExecutionContext fromExecutorService service

  @inline
  final implicit def javaExecutorToScalaExecutionContext(exec: Executor): ExecutionContext =
    ExecutionContext fromExecutor exec
}
