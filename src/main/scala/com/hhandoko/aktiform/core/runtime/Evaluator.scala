package com.hhandoko.aktiform.core.runtime

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

import cats.effect.{ContextShift, IO}
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json

import com.hhandoko.aktiform.api.task.{IOStep, Step}

object Evaluator extends LazyLogging {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  def run(steps: List[Step])(input: Json): Either[String, Json] =
    eval(IO.pure(input), steps).attempt
      .unsafeRunSync()
      .left
      .map(err => err.getMessage)

  @tailrec
  private[this] def eval(acc: IO[Json], steps: List[Step]): IO[Json] =
    steps match {
      case Nil =>
        acc

      case step :: Nil =>
        runNext(acc, step)

      case step :: tail =>
        eval(runNext(acc, step), tail)
    }

  private[this] def runNext(acc: IO[Json], step: Step): IO[Json] = {
    step match {
      case step: IOStep =>
        for {
          a <- acc
          r <- IO(logger.debug("[runNext] Shift -> Run")) *> cs.shift *> IO(step.run(a))
        } yield r

      case step =>
        for {
          a <- acc
          r <- IO(logger.debug("[runNext] Run")) *> IO(step.run(a))
        } yield r
    }
  }
}
