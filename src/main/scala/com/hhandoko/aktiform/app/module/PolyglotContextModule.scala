package com.hhandoko.aktiform.app.module

import org.graalvm.polyglot.Context
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * GraalVM polyglot feature context module loader.
 *
 * Creates polyglot context as a Singleton that will be passed to dependent classes, as context creation is an expensive
 * process.
 */
@Configuration
class PolyglotContextModule {

  @Bean
  def context: Context = Context.create("js")
}
