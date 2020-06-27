package com.hhandoko.aktiform.app.module

import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}

import com.hhandoko.aktiform.core.concurrent.MDCPropagatingExecutionContext

@Configuration
class ExecutionContextModule @Autowired() (
    executor: Executor
) {

  @Bean
  def executionContext: ExecutionContext =
    MDCPropagatingExecutionContext {
      ExecutionContext.fromExecutor(executor)
    }
}
