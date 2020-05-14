package com.hhandoko.aktiform.app.view.render

import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.api.html.input.{Form, FormField, InputTextField}
import com.hhandoko.aktiform.app.config.ResourcesVariantConfig

final class HtmlBootstrapRender(config: ResourcesVariantConfig)
    extends HtmlRender {

  private final val pageRender = new DynamicPageRender(config, render)

  def render(page: Page): String =
    pageRender.render(page)

  private[this] def render(el: Element): String = el match {
    case f: Form => render(f)
  }

  private[this] def render(field: FormField): String = field match {
    case t: InputTextField => bootstrap.InputTextComponent.render(t)
  }

  private[this] def render(form: Form): String =
    s"""<form class="needs-validation" action="${form.target}" method="post" novalidate>
       |${form.fields.map(render).mkString}
       |<button class="btn btn-primary btn-lg btn-block" type="submit">Submit</button>
       |</form>
       |""".stripMargin
}
