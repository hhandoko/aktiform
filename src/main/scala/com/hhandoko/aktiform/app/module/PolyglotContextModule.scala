package com.hhandoko.aktiform.app.module

import javax.annotation.PostConstruct
import scala.concurrent.{ExecutionContext, Future}

import com.typesafe.scalalogging.LazyLogging
import org.graalvm.polyglot.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}

import com.hhandoko.aktiform.core.Capabilities

/** GraalVM polyglot feature context module loader.
  *
  * Creates polyglot context as a Singleton that will be passed to dependent
  * classes, as context creation is an expensive process.
  *
  * TODO: To be refactored as context is not thread-safe
  * TODO: To be refactored to lazy loading, once flow building blocks is ready
  */
@Configuration
class PolyglotContextModule @Autowired() (
    executionContext: ExecutionContext
) extends LazyLogging {

  private final val VERSION_KEY = "java.vendor.version"
  private final val GRAAL_VM_ID = "graalvm"
  private final val JS_LANG_ID  = "js"

  @Bean
  def context: Context = {
    // TODO: Enforce lazy-loading of polyglot context
    if (Capabilities.polyglot) {
      logger.debug(s"[context] Creating polyglot context")

      Context.create(JS_LANG_ID)
    } else null
  }

  @PostConstruct
  def polyglotCapability(): Unit = {
    val version            = System.getProperty(VERSION_KEY)
    val hasPolyglotSupport = isGraalVm(version)

    if (hasPolyglotSupport) {
      logger.info(s"[polyglotCapability] Polyglot feature enabled, detected runtime '$version'")
      warmUp
    } else {
      logger.warn(s"[polyglotCapability] Polyglot feature not available, detected runtime is '$version'")
    }

    Capabilities.polyglot = hasPolyglotSupport
  }

  /** Warm-up the newly created polyglot context.
    *
    * Cold context takes over a second to initialise, compared to millisecond
    * of execution time.
    *
    * This method is fire-and-forget, and will be run on a separate thread as
    * not to block application startup.
    */
  private[this] def warmUp: Future[Unit] =
    Future {
      logger.debug(s"[warmUp] Warming up GraalVM polyglot context")

      val exec = context.eval(JS_LANG_ID, "x => x * x")
      (1 to 100).foreach { i =>
        exec.executeVoid(i)
      }

      logger.debug(s"[warmUp] GraalVM polyglot context warmed up")
    }(executionContext)

  /** Naive check whether the underlying Java runtime is GraalVM.
    *
    * @param version Java SDK vendor version.
    * @return True if the underlying Java runtime is GraalVM.
    */
  private[this] def isGraalVm(version: String): Boolean =
    version.trim.toLowerCase.startsWith(GRAAL_VM_ID)
}
