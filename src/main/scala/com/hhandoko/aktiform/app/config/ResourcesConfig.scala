package com.hhandoko.aktiform.app.config

import scala.beans.BeanProperty

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Configuration, PropertySource}

/** Static resources path from configuration values.
  *
  * This config allows styles and scripts to be loaded locally for development
  * purposes, and from CDN (default or User-provided, e.g. CloudFront) when
  * packaged and run for production use.
  *
  * @param bootstrap Bootstrap style and scripts.
  */
@Configuration
@ConfigurationProperties(prefix = "resources")
@PropertySource(value = Array("classpath:/resources.properties"))
@PropertySource(
  value = Array("classpath:/resources-${BOOT_ENV}.properties"),
  ignoreResourceNotFound = true
)
case class ResourcesConfig(
    @BeanProperty var bootstrap: ResourcesVariantConfig
) {
  // Workaround for no-args constructor
  def this() = this(new ResourcesVariantConfig())
}
