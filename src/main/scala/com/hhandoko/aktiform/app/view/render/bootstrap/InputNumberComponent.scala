package com.hhandoko.aktiform.app.view.render.bootstrap

import java.util.{List => JList}

import com.hhandoko.aktiform.api.html.input.{FormFieldError, InputNumberField}
import com.hhandoko.aktiform.app.view.render.InputComponent

object InputNumberComponent extends InputComponent {

  val templatePath = "templates/bootstrap/input-number.mustache"

  def render(field: InputNumberField): String =
    template.execute(InputNumberFieldModel(field))

  private[this] final case class InputNumberFieldModel(
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
  private[this] final object InputNumberFieldModel {
    import scala.jdk.CollectionConverters._

    def apply(domain: InputNumberField): InputNumberFieldModel =
      new InputNumberFieldModel(
        id = domain.id,
        name = domain.name,
        label = domain.label,
        required = domain.required,
        errors = domain.errors.asJava,
        value = domain.value.map(_.toString).orNull,
        placeholder = domain.placeholder.orNull
      )
  }
}
