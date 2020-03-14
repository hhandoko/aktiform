package com.hhandoko.aktiform.app.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}
import org.springframework.web.servlet.ModelAndView

import com.hhandoko.aktiform.app.config.ResourcesConfig

@RestController
class HomeController @Autowired()(
    resourcesConfig: ResourcesConfig
) {

  @GetMapping(value = Array("/"))
  def index(): ModelAndView = {
    import scala.jdk.CollectionConverters._
    val model = Map(
      "resources.bootstrapStylePath" -> resourcesConfig.bootstrapStylePath,
      "resources.bootstrapScriptPath" -> resourcesConfig.bootstrapScriptPath,
      "resources.jqueryScriptPath" -> resourcesConfig.jqueryScriptPath,
      "resources.popperScriptPath" -> resourcesConfig.popperScriptPath
    )
    new ModelAndView("home", model.asJava)
  }
}
