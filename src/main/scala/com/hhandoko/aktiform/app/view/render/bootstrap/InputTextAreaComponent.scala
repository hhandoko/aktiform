package com.hhandoko.aktiform.app.view.render.bootstrap

import java.util.{List => JList}

import com.hhandoko.aktiform.api.html.input.{FormFieldError, InputTextAreaField}
import com.hhandoko.aktiform.app.view.render.InputComponent

object InputTextAreaComponent extends InputComponent {

  val templatePath = "templates/bootstrap/input-textarea.mustache"

  def render(field: InputTextAreaField): String =
    template.execute(InputTextAreaFieldModel(field))

  private[this] final case class InputTextAreaFieldModel(
      id: String,
      name: String,
      label: String,
      required: Boolean,
      errors: JList[FormFieldError],
      value: String,
      placeholder: String
  ) {
    val has_single_error: Boolean    = errors.size() == 1
    val has_multiple_errors: Boolean = errors.size() > 1
  }
  private[this] final object InputTextAreaFieldModel {
    import scala.jdk.CollectionConverters._

    def apply(domain: InputTextAreaField): InputTextAreaFieldModel =
      new InputTextAreaFieldModel(
        domain.id,
        domain.name,
        domain.label,
        domain.required,
        domain.errors.asJava,
        domain.value.orNull,
        domain.placeholder.orNull
      )
  }
}
