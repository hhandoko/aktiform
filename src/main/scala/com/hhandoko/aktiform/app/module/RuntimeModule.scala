package com.hhandoko.aktiform.app.module

import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}

import com.hhandoko.aktiform.core.runtime.{Evaluator, IOEvaluator, MDCPropagatingExecutionContext}

/** Runtime module loader.
  *
  * Set up both `ExecutionContext` and `Evaluator` instance for use in the
  * runtime.
  *
  * Spring's default Executor is injected to the custom MDC-propagating
  * `ExecutionContext` and to the `Evaluator` for two main reasons:
  *
  * - Ensure MDC properties are retained when thread boundaries are crossed,
  *   i.e. context-switching.
  *
  * - Ensures that cats-effect IO uses the underlying Spring Boot thread group,
  *   which uses Undertow's XNIO worker threading implementation instead of
  *   Scala's global `ExecutionContext`.
  *
  * @param executor Executor for runnable task.
  */
@Configuration
class RuntimeModule @Autowired() (
    executor: Executor
) {

  @Bean
  def executionContext: ExecutionContext =
    MDCPropagatingExecutionContext {
      ExecutionContext.fromExecutor(executor)
    }

  @Bean
  def evaluator: Evaluator =
    new IOEvaluator(executionContext)
}
