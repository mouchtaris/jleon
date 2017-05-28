package gv.jleon2
package model.facade

import scala.concurrent.{ ExecutionContext }

trait ExecutionContexts {

  implicit val RequestProcessing: ExecutionContext

}
