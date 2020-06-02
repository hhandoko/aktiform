package com.hhandoko.aktiform.core.runtime

import scala.annotation.tailrec

import cats.effect.IO
import io.circe.Json

import com.hhandoko.aktiform.api.task.Step

object Evaluator {

  def run(steps: List[IO[Step]])(input: Json): Either[String, Json] =
    eval(IO(input), steps).attempt
      .unsafeRunSync()
      .left
      .map(err => err.getMessage)

  @tailrec
  private[this] def eval(acc: IO[Json], steps: List[IO[Step]]): IO[Json] =
    steps match {
      case Nil          => acc
      case step :: Nil  => runNext(acc, step)
      case step :: tail => eval(runNext(acc, step), tail)
    }

  private[this] def runNext(acc: IO[Json], step: IO[Step]): IO[Json] =
    for {
      a <- acc
      s <- step
    } yield s.run(a)
}
