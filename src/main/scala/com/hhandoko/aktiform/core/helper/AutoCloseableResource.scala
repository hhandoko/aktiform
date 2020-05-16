package com.hhandoko.aktiform.core.helper

import java.io.Closeable

object AutoCloseableResource {

  /** Convenience method for working with closeable resource.
    *
    * @param res Closeable resource.
    * @param fn Function that will operate using the given resource.
    * @tparam A Resource type parameter.
    * @tparam B Result type parameter.
    * @return Resource result.
    */
  def using[A <: Closeable, B](res: A)(fn: A => B): B =
    try fn(res)
    finally res.close()
}
