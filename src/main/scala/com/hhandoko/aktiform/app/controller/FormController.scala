package com.hhandoko.aktiform.app.controller

import java.util.{Map => JMap}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

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
import com.hhandoko.aktiform.api.task.Step
import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.app.view.render.HtmlBootstrapRender
import com.hhandoko.aktiform.core.Capabilities

@RestController
final class FormController @Autowired() (
    context: Context,
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

    val printStep     = PrintStep.run(formPayload)
    val transformStep = TransformStep.run(printStep)

    val stepsSeq = List(PrintStep.run _, TransformStep.run _)
    val stepSequence = stepsSeq.foldLeft(formPayload) {
      case (acc, step) => step(acc)
    }

    val stepsTryRec = List(Try(PrintStep.run _), Try(TransformStep.run _))
    val stepTryRecursive =
      recursive[Json, Try](
        formPayload,
        stepsTryRec,
        (acc, eff) => eff.map(fun => fun(acc)),
        eff => eff.toEither.left.map(_ => "Fail")
      ).fold(err => Json.fromString(err), identity)

    import scala.concurrent.ExecutionContext.Implicits.global
    val stepsFutRec = List(Future(PrintStep.run _), Future(TransformStep.run _))
    val stepFutRecursive =
      recursive[Json, Future](
        formPayload,
        stepsFutRec,
        (acc, eff) => eff.map(fun => fun(acc)),
        eff => Await.result(eff.map(res => Right(res)).recover { case _ => Left("Fail") }, 30.seconds)
      ).fold(err => Json.fromString(err), identity)

    data.asScala
      .map { case (key, value) => s"$key -> $value" }
      .mkString("<br>")
      .prependedAll("<h1>Form Data</h1>")
      .prependedAll(s"id -> $id")
      .prependedAll("<h1>Request</h1>")
      .concat("<h1>Print Step</h1>")
      .concat(printStep.spaces4SortKeys)
      .concat("<h1>Transform Step</h1>")
      .concat(transformStep.spaces4SortKeys)
      .concat("<h1>Combined</h1>")
      .concat(stepSequence.spaces4SortKeys)
      .concat("<h1>Recursive (Try)</h1>")
      .concat(stepTryRecursive.spaces4SortKeys)
      .concat("<h1>Recursive (Future)</h1>")
      .concat(stepFutRecursive.spaces4SortKeys)
  }

  private def recursive[A, F[_]](
      acc: A,
      steps: List[F[A => A]],
      map: (A, F[A => A]) => F[A],
      eval: F[A] => Either[String, A]
  ): Either[String, A] = {
    steps match {
      case Nil =>
        Right(acc)

      case step :: Nil =>
        eval(map(acc, step))

      case step :: tail =>
        eval(map(acc, step))
          .fold(
            err => Left(err),
            res => recursive(res, tail, map, eval)
          )
    }
  }

  object PrintStep extends Step {
    override def run(payload: Json): Json = {
      payload.hcursor.downField("data").keys.foreach(println)
      payload
    }
  }

  object TransformStep extends Step {
    override def run(payload: Json): Json = {
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
