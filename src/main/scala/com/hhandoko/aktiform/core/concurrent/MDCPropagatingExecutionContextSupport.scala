package com.hhandoko.aktiform.core.concurrent

import java.util.{Map => JMap}
import scala.concurrent.ExecutionContext

import org.slf4j.MDC

/** Scala's `ExecutionContext`` helper trait, to ensure Slf4j's Mapped Diagnostic
  * Context (MDC) values are propagated to the execution site, retaining
  * `ThreadLocal`-like MDC logging behaviour for async operations.
  *
  * MDC does not work well in async context. MDC will output blank values as
  * Scala's `ExecutionContext`` will execute on a shared pool of worker threads,
  * s opposed to MDC defaults which bound their values to each thread
  * (`ThreadLocal`).
  *
  * Refer to: https://medium.com/hootsuite-engineering/logging-contextual-info-in-an-asynchronous-scala-application-8ea33bfec9b3
  */
trait MDCPropagatingExecutionContextSupport extends ExecutionContext { self =>

  override def prepare(): ExecutionContext = new ExecutionContext {
    // Save the call-site MDC state
    val context: JMap[String, String] = MDC.getCopyOfContextMap

    override def execute(runnable: Runnable): Unit =
      self.execute(() => {
        // Save the existing execution-site MDC state
        val originalContext = MDC.getCopyOfContextMap
        try {
          // Set the call-site MDC state into the execution-site MDC
          if (context != null) MDC.setContextMap(context)
          else MDC.clear()

          runnable.run()
        } finally {
          // Restore the existing execution-site MDC state
          if (originalContext != null) MDC.setContextMap(originalContext)
          else MDC.clear()
        }
      })

    override def reportFailure(cause: Throwable): Unit =
      self.reportFailure(cause)
  }
}
