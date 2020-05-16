package com.hhandoko.aktiform.app.view.render

import com.hhandoko.aktiform.api.html.input.Form
import com.hhandoko.aktiform.api.html.{Element, Page, Section}
import com.hhandoko.aktiform.app.config.ResourcesVariantConfig
import com.hhandoko.aktiform.app.view.render.bootstrap.{FormComponent, SectionComponent}

/** HTML renderer using Bootstrap (v4.x) styles.
  *
  * @param config Bootstrap resources loaded via config.
  */
final class HtmlBootstrapRender(config: ResourcesVariantConfig)
    extends HtmlRender {

  private final val pageRender = new DynamicPageRender(config, render)
  private final val sectionRender = new SectionComponent(render)
  private final val formRender = FormComponent

  def render(page: Page): String =
    pageRender.render(page)

  private[this] def render(el: Element): String = el match {
    case s: Section => sectionRender.render(s)
    case f: Form => formRender.render(f)
  }
}
