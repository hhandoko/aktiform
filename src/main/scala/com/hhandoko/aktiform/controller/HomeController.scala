package com.hhandoko.aktiform.controller

import org.springframework.web.bind.annotation.{GetMapping, RestController}
import org.springframework.web.servlet.ModelAndView

@RestController
class HomeController {

  @GetMapping(value = Array("/"))
  def index(): ModelAndView = {
    new ModelAndView("home")
  }
}
