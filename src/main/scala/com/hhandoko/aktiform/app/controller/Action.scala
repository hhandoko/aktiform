package com.hhandoko.aktiform.app.controller

import java.util.concurrent.Callable

import cats.effect.IO

/** Convenient wrapper to evaluate IO[_] to synchronous or asynchronously in
  * controller.
  */
object Action {

  /** Invoke controller response synchronously.
    *
    * Web server thread will be held until response is ready to be returned,
    * regardless whether context switch happens during processing.
    *
    * @param res Controller response body expression.
    * @tparam T Response type.
    * @return Controller response body.
    */
  def sync[T](res: => IO[T]): T =
    res.unsafeRunSync()

  /** Invoke controller response asynchronously.
    *
    * When invoked, web server thread will be released to process another
    * client. Compared to the synchronous method, there is some additional
    * overhead when switching context from processing thread back to the web
    * server thread.
    *
    * @param res Controller response body expression.
    * @tparam T Response type.
    * @return Controller response body.
    */
  def async[T](res: => IO[T]): Callable[T] =
    () => res.unsafeRunSync()
}
