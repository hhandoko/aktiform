package com.hhandoko.aktiform.app.config.resource

import java.util.{List => JList}
import scala.beans.BeanProperty

/** Read variant-specific list of styles and scripts to be loaded.
  *
  * This configuration reader is designed to be generic, mainly to support
  * different styles in the future, e.g. Bootstrap V4 and Bootstrap V5 scripts.
  *
  * @param styles Paths to CSS.
  * @param scripts Paths to scripts.
  */
final case class ResourceVariant(
    @BeanProperty var styles: JList[Resource],
    @BeanProperty var scripts: JList[Resource]
) {
  // Workaround for no-args constructor
  def this() = this(null, null)
}
