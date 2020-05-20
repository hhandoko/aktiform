package com.hhandoko.aktiform.app.module

import com.typesafe.scalalogging.LazyLogging
import org.graalvm.polyglot.Context
import org.springframework.context.annotation.{Bean, Configuration}

import com.hhandoko.aktiform.core.Capabilities

/** GraalVM polyglot feature context module loader.
  *
  * Creates polyglot context as a Singleton that will be passed to dependent classes, as context creation is an expensive
  * process.
  *
  * TODO: To be refactored as context is not thread-safe
  */
@Configuration
class PolyglotContextModule extends LazyLogging {

  private final val VERSION_KEY = "java.vendor.version"
  private final val GRAAL_VM_ID = "graalvm"

  @Bean
  def context: Context = {
    val version = System.getProperty(VERSION_KEY)
    // TODO: Should not return null, ever. Will be refactored once workflow is
    //       developed (alongside thread-safe Context execution).
    if (isGraalVm(version)) {
      logger.info(s"[context] Polyglot feature enabled, detected runtime '$version'")
      Capabilities.polyglot = true
      Context.create("js")
    } else {
      logger.warn(s"[context] Polyglot feature disabled, detected runtime is '$version'")
      Capabilities.polyglot = false
      null
    }
  }

  private[this] def isGraalVm(version: String): Boolean =
    version.trim.toLowerCase.startsWith(GRAAL_VM_ID)
}
