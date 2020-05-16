package com.hhandoko.aktiform.app.view.render.bootstrap

import com.hhandoko.aktiform.api.html.input.{Form, FormField, InputTextField}
import com.hhandoko.aktiform.app.view.render.RenderComponent

object FormComponent extends RenderComponent {

  val templatePath = "templates/bootstrap/form.mustache"

  def render(form: Form): String =
    template.execute(FormModel(form))

  private[this] final case class FormModel(
      target: String,
      fields: String
  )
  private[this] final object FormModel {
    def apply(form: Form): FormModel =
      FormModel(
        form.target,
        form.fields.map(render).mkString
      )
  }

  private[this] def render(field: FormField): String = field match {
    case t: InputTextField => InputTextComponent.render(t)
  }
}
