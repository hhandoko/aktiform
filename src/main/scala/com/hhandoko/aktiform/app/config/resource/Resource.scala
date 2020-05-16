package com.hhandoko.aktiform.app.config.resource

import scala.beans.BeanProperty

/** Resource to load, e.g. script or stylesheet.
  *
  * @param path Path to the resource (local or remote).
  * @param hash Checksum signature of the resource.
  */
case class Resource(
    @BeanProperty var path: String,
    @BeanProperty var hash: String
) {
  // Workaround for no-args constructor
  def this() = this(null, null)
}
