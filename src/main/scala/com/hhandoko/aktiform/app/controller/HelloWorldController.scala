package com.hhandoko.aktiform.app.controller

import java.util.{Map => JMap}

import org.springframework.web.bind.annotation.{
  GetMapping,
  PathVariable,
  RestController
}
import org.springframework.web.servlet.ModelAndView

@RestController
class HelloWorldController {

  @GetMapping(value = Array("/hello/{name}"))
  def hello(
      @PathVariable name: String,
      model: JMap[String, AnyRef]
  ): ModelAndView = {
    model.put("name", name)
    new ModelAndView("hello", model)
  }
}
