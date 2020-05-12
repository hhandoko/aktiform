package com.hhandoko.aktiform.app.view.render

import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.api.html.input.{Form, FormField, InputTextField}

object HtmlBootstrapRender extends HtmlRender {

  def render(page: Page): String =
    s"""<!doctype html>
       |<html lang="en">
       |<head>
       |</head>
       |<body>
       |    ${page.els.map(render).mkString}
       |</body>
       |</html>
       |""".stripMargin

  def render(el: Element): String = el match {
    case f: Form => render(f)
  }

  def render(field: FormField): String = field match {
    case t: InputTextField => render(t)
  }

  private[this] def render(form: Form): String =
    s"""<form class="needs-validation" action="${form.target}" method="post" novalidate>
       |${form.fields.map(render).mkString}
       |<button class="btn btn-primary btn-lg btn-block" type="submit">Submit</button>
       |</form>
       |""".stripMargin

  private[this] def render(field: InputTextField): String =
    s"""<label for="${field.id}">${field.label}</label>
       |<input id="${field.id}"
       |       name="${field.name}"
       |       class="form-control"
       |       type="text">
       |""".stripMargin
}
