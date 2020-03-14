package com.hhandoko.aktiform

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.app.module.PolyglotContextModule

@EnableConfigurationProperties(
  value = Array(
    classOf[ResourcesConfig]
  )
)
@Import(
  value = Array(
    classOf[PolyglotContextModule]
  )
)
@SpringBootApplication
class AktiformApplication {}

object AktiformApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[AktiformApplication], args: _*)
    ()
  }
}
