package com.hhandoko.aktiform.app.controller

import java.util.{Map => JMap}

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{
  GetMapping,
  PathVariable,
  PostMapping,
  RequestParam,
  ResponseBody,
  RestController
}

import com.hhandoko.aktiform.api.html.Page
import com.hhandoko.aktiform.api.html.input.{Form, InputTextField}
import com.hhandoko.aktiform.app.view.render.HtmlBootstrapRender

@RestController
class FormController @Autowired()(
    ) {

  @GetMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def showForm(
      @PathVariable id: String
  ): String = {
    val form =
      Form(s"/forms/${id}", Seq(InputTextField("name", "name", "Name")))
    val page = Page(Seq(form))
    HtmlBootstrapRender.render(page)
  }

  @PostMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def processForm(
      @PathVariable id: String,
      @RequestParam data: JMap[String, String]
  ): String = {
    import scala.jdk.CollectionConverters._
    data.asScala.map { case (key, value) => s"$key -> $value" }.mkString("<br>")
  }
}