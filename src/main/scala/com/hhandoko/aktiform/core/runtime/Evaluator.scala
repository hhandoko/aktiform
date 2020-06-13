package com.hhandoko.aktiform.core.runtime

import scala.annotation.tailrec

import cats.effect.{ContextShift, IO}
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json

import com.hhandoko.aktiform.api.task.{IOStep, Step}
import com.hhandoko.aktiform.core.concurrent.MDCPropagatingExecutionContext

object Evaluator extends LazyLogging {

  implicit val cs: ContextShift[IO] = IO.contextShift(MDCPropagatingExecutionContext.global)

  def run(steps: List[Step[_, _]])(input: Json): Either[String, Json] =
    eval(IO.pure(input), steps).attempt
      .unsafeRunSync()
      .left
      .map(err => err.getMessage)

  @tailrec
  private[this] def eval(acc: IO[Json], steps: List[Step[_, _]]): IO[Json] =
    steps match {
      case Nil =>
        acc

      case step :: Nil =>
        logger.debug(s"[eval] Final: ${step.runtimeClassOf.getCanonicalName}")
        runNext(acc, step)

      case step :: tail =>
        logger.debug(s"[eval] Step: ${step.runtimeClassOf.getCanonicalName}")
        eval(runNext(acc, step), tail)
    }

  private[this] def runNext(acc: IO[Json], step: Step[_, _]): IO[Json] = {
    step match {
      case step: IOStep[_, _] =>
        for {
          a <- acc
          r <- IO(logger.debug("[runNext] Shift -> Run")) *> cs.shift *> IO(step.map(a))
        } yield {
          logger.debug(s"[runNext] IO - ${a.getClass} vs ${step.runtimeClassOf}")
          logger.debug(s"[runNext] IO - Same? ${"Hello".getClass.isAssignableFrom(step.runtimeClassOf)}")
          r
        }

      case step =>
        for {
          a <- acc
          r <- IO(logger.debug("[runNext] Run")) *> IO(step.map(a))
        } yield {
          logger.debug(s"[runNext] Standard - ${a.getClass} vs ${step.runtimeClassOf}")
          logger.debug(s"[runNext] Standard - Same? ${"Hello".getClass.isAssignableFrom(step.runtimeClassOf)}")
          r
        }
    }
  }
}
