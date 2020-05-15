package com.hhandoko.aktiform.app.view.render

import com.hhandoko.aktiform.api.html.input.Form
import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.app.config.ResourcesVariantConfig

/** HTML renderer using Bootstrap (v4.x) styles.
  *
  * @param config Bootstrap resources loaded via config.
  */
final class HtmlBootstrapRender(config: ResourcesVariantConfig)
    extends HtmlRender {

  private final val pageRender = new DynamicPageRender(config, render)

  def render(page: Page): String =
    pageRender.render(page)

  private[this] def render(el: Element): String = el match {
    case f: Form => bootstrap.FormComponent.render(f)
  }
}
