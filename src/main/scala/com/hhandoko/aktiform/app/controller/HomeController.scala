package com.hhandoko.aktiform.app.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}
import org.springframework.web.servlet.ModelAndView

import com.hhandoko.aktiform.app.config.ResourcesConfig

@RestController
final class HomeController @Autowired() (
    resourcesConfig: ResourcesConfig
) {

  @GetMapping(value = Array("/"))
  def index(): ModelAndView = {
    import scala.jdk.CollectionConverters._
    val model = Map(
      "styles"  -> resourcesConfig.bootstrap.styles,
      "scripts" -> resourcesConfig.bootstrap.scripts
    )
    new ModelAndView("home", model.asJava)
  }
}
