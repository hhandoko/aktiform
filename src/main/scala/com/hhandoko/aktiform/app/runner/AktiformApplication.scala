package com.hhandoko.aktiform.app.runner

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.app.controller.{
  FormController,
  HelloWorldController,
  HomeController
}
import com.hhandoko.aktiform.app.module.PolyglotContextModule

/** Main application class (Spring Application runner).
  *
  * `EnableConfigurationProperties` and `Import` is used here to make it explicit configuration and modules that are
  * loaded than relying on automated component scanning.
  */
@EnableConfigurationProperties(
  value = Array(
    classOf[ResourcesConfig]
  )
)
@Import(
  value = Array(
    // Modules
    classOf[PolyglotContextModule],
    // Controllers
    classOf[FormController],
    classOf[HelloWorldController],
    classOf[HomeController]
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
