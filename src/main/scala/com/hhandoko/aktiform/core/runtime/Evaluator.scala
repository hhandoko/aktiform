package com.hhandoko.aktiform.core.runtime

import cats.effect.IO
import io.circe.Json

object Evaluator {

  def run(steps: List[IO[Json => Json]])(input: Json): Either[String, Json] = {
    recursive[Json, IO](
      input,
      steps,
      (acc, eff) => eff.map(fun => fun(acc)),
      eff => eff.attempt.unsafeRunSync().left.map(_ => "Fail")
    )
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
}
