package com.hhandoko.aktiform.app.module

import org.graalvm.polyglot.Context
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class PolyglotContextModule {

  @Bean
  def context: Context = Context.create("js")
}
