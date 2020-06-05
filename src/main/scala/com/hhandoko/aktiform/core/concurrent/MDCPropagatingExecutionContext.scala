package com.hhandoko.aktiform.core.concurrent

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

/** Convenience class to wrap given `ExecutionContext` with MDC-propagation
  * capabilities.
  *
  * @param wrapped ExecutionContext to wrap and enhance with MDC-propagation.
  */
class MDCPropagatingExecutionContext(wrapped: ExecutionContext)
    extends ExecutionContext
    with MDCPropagatingExecutionContextSupport {

  override def execute(runnable: Runnable): Unit =
    wrapped.execute(runnable)

  override def reportFailure(cause: Throwable): Unit =
    wrapped.reportFailure(cause)
}

object MDCPropagatingExecutionContext {

  private final val EVALUATOR_EC =
    MDCPropagatingExecutionContext {
      ExecutionContext.fromExecutor {
        Executors.newWorkStealingPool()
      }
    }

  def apply(wrapped: ExecutionContext): MDCPropagatingExecutionContext =
    new MDCPropagatingExecutionContext(wrapped)

  val global: MDCPropagatingExecutionContext = EVALUATOR_EC

  object Implicits {
    // Convenience wrapper around the Scala global ExecutionContext
    implicit lazy val global: MDCPropagatingExecutionContext = EVALUATOR_EC
  }
}
