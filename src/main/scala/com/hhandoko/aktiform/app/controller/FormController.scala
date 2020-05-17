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

import com.hhandoko.aktiform.api.html.input.{Form, InputTextAreaField, InputTextField}
import com.hhandoko.aktiform.api.html.{Page, Section}
import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.app.view.render.HtmlBootstrapRender

@RestController
final class FormController @Autowired() (
    resourcesConfig: ResourcesConfig
) {

  private final val renderer = new HtmlBootstrapRender(
    resourcesConfig.bootstrap
  )

  @GetMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def showForm(
      @PathVariable id: String
  ): String = {
    val textField     = InputTextField("name", "name", "Name")
    val textAreaField = InputTextAreaField("notes", "notes", "Notes")
    val fields        = Seq(textField, textAreaField)
    val form          = Form(s"/forms/${id}", fields)
    val section       = Section(Seq(form))
    val page          = Page(Seq(section))

    renderer.render(page)
  }

  @PostMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def processForm(
      @PathVariable id: String,
      @RequestParam data: JMap[String, String]
  ): String = {
    import scala.jdk.CollectionConverters._

    data.asScala
      .map { case (key, value) => s"$key -> $value" }
      .mkString("<br>")
      .concat(s"<br>")
      .concat(s"id -> $id")
  }
}
