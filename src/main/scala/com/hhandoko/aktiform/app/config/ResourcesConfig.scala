package com.hhandoko.aktiform.app.config

import scala.beans.BeanProperty

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Configuration, PropertySource}

@Configuration
@ConfigurationProperties(prefix = "resources")
@PropertySource(value = Array("classpath:/resources.properties"))
@PropertySource(
  value = Array("classpath:/resources-${BOOT_ENV}.properties"),
  ignoreResourceNotFound = true
)
class ResourcesConfig(
    @BeanProperty var bootstrapPath: String
) {
  // Workaround for no-args constructor
  def this() = this("")
}
