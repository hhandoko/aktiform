package com.hhandoko.aktiform.app.config

import scala.beans.BeanProperty

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Configuration, PropertySource}

/**
 * Static resources path from configuration values.
 *
 * This config allows styles and scripts to be loaded locally for development purposes, and from CDN (default or User-
 * provided, e.g. CloudFront) when packaged and run for production use.
 *
 * TODO: Convert hardcoded Bootstrap styles and script path to string array configuration with the checksum, to allow
 *       more flexible override
 *
 * @param bootstrapStylePath Path to Bootstrap CSS.
 * @param bootstrapScriptPath Path to Bootstrap (plugin) script.
 * @param jqueryScriptPath Path to jQuery script.
 * @param popperScriptPath Path to Popper script.
 */
@Configuration
@ConfigurationProperties(prefix = "resources")
@PropertySource(value = Array("classpath:/resources.properties"))
@PropertySource(
  value = Array("classpath:/resources-${BOOT_ENV}.properties"),
  ignoreResourceNotFound = true
)
case class ResourcesConfig(
    @BeanProperty var bootstrapStylePath: String,
    @BeanProperty var bootstrapScriptPath: String,
    @BeanProperty var jqueryScriptPath: String,
    @BeanProperty var popperScriptPath: String
) {
  // Workaround for no-args constructor
  def this() = this("", "", "", "")
}
