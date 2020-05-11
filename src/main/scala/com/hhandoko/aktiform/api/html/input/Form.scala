package com.hhandoko.aktiform.api.html.input

import com.hhandoko.aktiform.api.html.Element

final case class Form(target: String, fields: Seq[FormField]) extends Element {
  def render(): String = {
    s"""<form class="needs-validation" action="${target}" method="post" novalidate>
       |${fields.map(_.render()).mkString}
       |<button class="btn btn-primary btn-lg btn-block" type="submit">Submit</button>
       |</form>
       |""".stripMargin
  }
}
