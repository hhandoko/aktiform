package com.hhandoko.aktiform.app.view.render.bootstrap

import java.util.{List => JList}

import com.hhandoko.aktiform.api.html.input.{FormFieldError, InputTextField}
import com.hhandoko.aktiform.app.view.render.InputComponent

object InputTextComponent extends InputComponent {

  val templatePath = "templates/bootstrap/input-text.mustache"

  def render(field: InputTextField): String =
    template.execute(InputTextFieldModel(field))

  private[this] final case class InputTextFieldModel(
      id: String,
      name: String,
      label: String,
      required: Boolean,
      errors: JList[FormFieldError],
      value: String,
      placeholder: String
  ) {
    val has_error: Boolean           = !errors.isEmpty
    val has_single_error: Boolean    = errors.size() == 1
    val has_multiple_errors: Boolean = errors.size() > 1
  }
  private[this] final object InputTextFieldModel {
    import scala.jdk.CollectionConverters._

    def apply(domain: InputTextField): InputTextFieldModel =
      new InputTextFieldModel(
        id = domain.id,
        name = domain.name,
        label = domain.label,
        required = domain.required,
        errors = domain.errors.asJava,
        value = domain.value.orNull,
        placeholder = domain.placeholder.orNull
      )
  }
}
