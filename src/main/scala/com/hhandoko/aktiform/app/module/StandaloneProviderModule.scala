package com.hhandoko.aktiform.app.module

import org.springframework.context.annotation.{Bean, Configuration}

import com.hhandoko.aktiform.core.repository.FormRepository
import com.hhandoko.aktiform.provider.standalone.repository.InMemoryFormRepository

/** Standalone providers module loader.
  *
  * Providers to support standalone (no external dependencies) deployment.
  *
  * TODO: Should be split into separate concern in the future, e.g. repository
  *       with its own module loader, but loads standalone where it supports it.
  */
@Configuration
class StandaloneProviderModule {

  @Bean
  def formRepo: FormRepository =
    new InMemoryFormRepository
}
