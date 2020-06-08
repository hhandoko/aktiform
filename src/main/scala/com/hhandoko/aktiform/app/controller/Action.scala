package com.hhandoko.aktiform.app.controller

import java.util.concurrent.Callable

import cats.effect.IO

object Action {

  def sync[T](res: => IO[T]): T =
    res.unsafeRunSync()

  def async[T](res: => IO[T]): Callable[T] =
    () => res.unsafeRunSync()
}
