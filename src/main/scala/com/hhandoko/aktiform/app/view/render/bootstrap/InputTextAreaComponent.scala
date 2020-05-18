package com.hhandoko.aktiform.app.view.render.bootstrap

import java.lang.{Integer => JInt}
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
      placeholder: String,
      rows: JInt
  ) {
    val has_error: Boolean           = !errors.isEmpty
    val has_single_error: Boolean    = errors.size() == 1
    val has_multiple_errors: Boolean = errors.size() > 1
  }
  private[this] final object InputTextAreaFieldModel {
    import scala.jdk.CollectionConverters._

    import com.hhandoko.aktiform.core.syntax.JavaPrimitiveExtension.OptionIntToJavaIntegerSyntax

    def apply(domain: InputTextAreaField): InputTextAreaFieldModel =
      new InputTextAreaFieldModel(
        id = domain.id,
        name = domain.name,
        label = domain.label,
        required = domain.required,
        errors = domain.errors.asJava,
        value = domain.value.orNull,
        placeholder = domain.placeholder.orNull,
        rows = domain.rows.orIntNull
      )
  }
}
