package com.hhandoko.aktiform.app.controller

import java.util.{Map => JMap}

import com.typesafe.scalalogging.LazyLogging
import io.circe.Json
import org.graalvm.polyglot.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{
  GetMapping,
  PathVariable,
  PostMapping,
  RequestParam,
  ResponseBody,
  RestController
}

import com.hhandoko.aktiform.api.html.input.{Form, InputNumberField, InputTextAreaField, InputTextField}
import com.hhandoko.aktiform.api.html.{Page, Section}
import com.hhandoko.aktiform.api.task.IOStep
import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.app.view.render.HtmlBootstrapRender
import com.hhandoko.aktiform.core.Capabilities
import com.hhandoko.aktiform.core.runtime.Evaluator

@RestController
final class FormController @Autowired() (
    context: Context,
    resourcesConfig: ResourcesConfig
) extends LazyLogging {

  private final val renderer = new HtmlBootstrapRender(
    resourcesConfig.bootstrap
  )

  @GetMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def showForm(
      @PathVariable id: String
  ): String = {
    val section = Section(Seq(form(id)))
    val page    = Page(Seq(section))

    renderer.render(page)
  }

  @PostMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def processForm(
      @PathVariable id: String,
      @RequestParam data: JMap[String, String]
  ): String = {
    import scala.jdk.CollectionConverters._

    val filledForm  = form(id).fill(data.asScala.toMap)
    val formPayload = filledForm.toJson

    val stepsIORec    = List(PrintStep, TransformStep, AlertStep)
    val stepsIOEval   = Evaluator.run(stepsIORec) _
    val stepsIOResult = stepsIOEval(formPayload).fold(Json.fromString, identity)

    data.asScala
      .map { case (key, value) => s"$key -> $value" }
      .mkString("<br>")
      .prependedAll("<h1>Form Data</h1>")
      .prependedAll(s"id -> $id")
      .prependedAll("<h1>Request</h1>")
      .concat("<h1>Recursive (IO)</h1>")
      .concat(stepsIOResult.spaces4SortKeys)
  }

  object AlertStep extends IOStep {
    override def run(payload: Json): Json = {
      logger.info(s"[AlertStep#run] Alert")

      payload
    }
  }

  object PrintStep extends IOStep {
    override def run(payload: Json): Json = {
      logger.info(s"[PrintStep#run] Print payload")

      payload.hcursor
        .downField("data")
        .keys
        .foreach { k =>
          logger.debug(s"[PrintStep#run] Keys: ${k.mkString(", ")}")
        }

      payload
    }
  }

  object TransformStep extends IOStep {
    override def run(payload: Json): Json = {
      logger.info(s"[TransformStep#run] Transform payload")

      // TODO: Convert to alert and disable form if capability does not exist
      if (Capabilities.polyglot) {
        val transformer = payload.hcursor.downField("data").get[String]("transform").getOrElse("x => x")
        val evaluator   = context.eval("js", transformer)
        val input       = payload.hcursor.downField("data").get[Int]("age").getOrElse(0)
        val result      = evaluator.execute(input).asInt()

        payload.hcursor
          .downField("data")
          .downField("age")
          .set(Json.fromInt(result))
          .top
          .get
      } else {
        payload
      }
    }
  }

  private def form(id: String): Form = {
    val textField      = InputTextField("name", "name", "Name")
    val textAreaField  = InputTextAreaField("notes", "notes", "Notes")
    val numberField    = InputNumberField("age", "age", "Age")
    val transformField = InputTextAreaField("transform", "transform", "Transform Data")
    val fields         = Seq(textField, textAreaField, numberField, transformField)

    Form(s"/forms/${id}", fields)
  }
}
