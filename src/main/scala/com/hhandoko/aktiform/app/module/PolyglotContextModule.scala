package com.hhandoko.aktiform.app.module

import javax.annotation.PostConstruct

import com.typesafe.scalalogging.LazyLogging
import org.graalvm.polyglot.Context
import org.springframework.context.annotation.{Bean, Configuration, Lazy}

import com.hhandoko.aktiform.core.Capabilities

/** GraalVM polyglot feature context module loader.
  *
  * Creates polyglot context as a Singleton that will be passed to dependent
  * classes, as context creation is an expensive process.
  *
  * TODO: To be refactored as context is not thread-safe
  */
@Configuration
class PolyglotContextModule extends LazyLogging {

  private final val VERSION_KEY = "java.vendor.version"
  private final val GRAAL_VM_ID = "graalvm"

  @Bean
  @Lazy(true)
  def context: Context = Context.create("js")

  @PostConstruct
  def polyglotCapability(): Unit = {
    val version            = System.getProperty(VERSION_KEY)
    val hasPolyglotSupport = isGraalVm(version)

    if (hasPolyglotSupport) logger.info(s"[polyglotCapability] Polyglot feature enabled, detected runtime '$version'")
    else logger.warn(s"[polyglotCapability] Polyglot feature not available, detected runtime is '$version'")

    Capabilities.polyglot = hasPolyglotSupport
  }

  private[this] def isGraalVm(version: String): Boolean =
    version.trim.toLowerCase.startsWith(GRAAL_VM_ID)
}
