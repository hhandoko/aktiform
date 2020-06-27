package com.hhandoko.aktiform.app.controller

import java.util.concurrent.Callable
import java.util.{Map => JMap}

import cats.effect.IO
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

import com.hhandoko.aktiform.api.html.{Page, Section}
import com.hhandoko.aktiform.api.task.IOStep
import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.app.view.render.HtmlBootstrapRender
import com.hhandoko.aktiform.core.Capabilities
import com.hhandoko.aktiform.core.repository.FormRepository
import com.hhandoko.aktiform.core.runtime.Evaluator

@RestController
final class FormController @Autowired() (
    context: Context,
    evaluator: Evaluator,
    formRepo: FormRepository,
    resourcesConfig: ResourcesConfig
) extends LazyLogging {

  private final val renderer = new HtmlBootstrapRender(
    resourcesConfig.bootstrap
  )

  @GetMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def showForm(
      @PathVariable id: String
  ): Callable[String] = Action.async {
    val section = Section(Seq(formRepo.getForm(id)))
    val page    = Page(Seq(section))

    val response = renderer.render(page)

    IO(response)
  }

  @PostMapping(value = Array("/forms/{id}"))
  @ResponseBody
  def processForm(
      @PathVariable id: String,
      @RequestParam data: JMap[String, String]
  ): Callable[String] = Action.async {
    import scala.jdk.CollectionConverters._

    val filledForm  = formRepo.getForm(id).fill(data.asScala.toMap)
    val formPayload = filledForm.toJson

    val stepsIORec    = List(PrintStep, TransformStep, AlertStep)
    val stepsIOEval   = evaluator.run(stepsIORec) _
    val stepsIOResult = stepsIOEval(formPayload).fold(Json.fromString, identity)

    val response =
      data.asScala
        .map { case (key, value) => s"$key -> $value" }
        .mkString("<br>")
        .prependedAll("<h1>Form Data</h1>")
        .prependedAll(s"id -> $id")
        .prependedAll("<h1>Request</h1>")
        .concat("<h1>Recursive (IO)</h1>")
        .concat(stepsIOResult.spaces4SortKeys)

    IO(response)
  }

  object AlertStep extends IOStep[Json, Json] {

    override def decode(in: Json): Json = in
    override def encode(in: Json): Json = in

    override def map(payload: Json): Json = {
      logger.info(s"[AlertStep#run] Alert")

      payload
    }

    override def run(payload: Json): Json =
      map(payload)
  }

  object PrintStep extends IOStep[Json, Json] {

    override def decode(in: Json): Json = in
    override def encode(in: Json): Json = in

    override def map(payload: Json): Json = {
      logger.info(s"[PrintStep#run] Print payload")

      payload.hcursor
        .downField("data")
        .keys
        .foreach { k =>
          logger.debug(s"[PrintStep#run] Keys: ${k.mkString(", ")}")
        }

      payload
    }

    override def run(payload: Json): Json =
      map(payload)
  }

  object TransformStep extends IOStep[Json, Json] {

    override def decode(in: Json): Json = in
    override def encode(in: Json): Json = in

    override def map(payload: Json): Json = {
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

    override def run(payload: Json): Json =
      map(payload)
  }
}
