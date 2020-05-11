package com.hhandoko.aktiform.api.html.input

sealed trait FormField {
  def render(): String
}

final case class InputTextField(id: String, name: String, label: String)
    extends FormField {
  def render(): String = {
    s"""<label for="${id}">${label}</label>
       |<input id="${id}"
       |       name="${name}"
       |       class="form-control"
       |       type="text">
       |""".stripMargin
  }
}
