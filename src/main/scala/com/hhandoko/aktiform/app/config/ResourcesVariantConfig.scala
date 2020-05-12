package com.hhandoko.aktiform.app.config

import java.util.{ArrayList => JArrayList, List => JList}
import scala.beans.BeanProperty

/** Read variant-specific list of styles and scripts to be loaded.
  *
  * This configuration reader is designed to be generic, mainly to support
  * different styles in the future, e.g. Bootstrap V4 and Bootstrap V5 scripts.
  *
  * TODO: Add checksum into configuration read as well.
  *
  * @param styles Paths to CSS.
  * @param scripts Paths to scripts.
  */
case class ResourcesVariantConfig(
    @BeanProperty var styles: JList[String],
    @BeanProperty var scripts: JList[String]
) {
  // Workaround for no-args constructor
  def this() = this(new JArrayList, new JArrayList)
}
