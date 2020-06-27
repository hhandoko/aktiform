package com.hhandoko.aktiform.core.concurrent

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

  def apply(wrapped: ExecutionContext): MDCPropagatingExecutionContext =
    new MDCPropagatingExecutionContext(wrapped)
}
