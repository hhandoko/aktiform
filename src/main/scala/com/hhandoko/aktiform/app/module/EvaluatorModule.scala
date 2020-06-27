package com.hhandoko.aktiform.app.module

import scala.concurrent.ExecutionContext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}

import com.hhandoko.aktiform.core.runtime.{Evaluator, IOEvaluator}

@Configuration
class EvaluatorModule @Autowired() (
    executionContext: ExecutionContext
) {

  @Bean
  def evaluator: Evaluator =
    new IOEvaluator(executionContext)
}
