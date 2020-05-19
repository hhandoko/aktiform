package com.hhandoko.aktiform.app.view.render.bootstrap

import java.util.{List => JList}

import com.hhandoko.aktiform.api.html.input.{Form, FormField, InputNumberField, InputTextAreaField, InputTextField}
import com.hhandoko.aktiform.app.view.render.RenderComponent

object FormComponent extends RenderComponent {

  val templatePath = "templates/bootstrap/form.mustache"

  def render(form: Form): String =
    template.execute(FormModel(form))

  private[this] final case class FormModel(
      target: String,
      fields: JList[String]
  )
  private[this] final object FormModel {
    import scala.jdk.CollectionConverters._
    def apply(form: Form): FormModel =
      FormModel(
        form.target,
        form.fields.map(render).asJava
      )
  }

  private[this] def render(field: FormField): String =
    field match {
      case t: InputTextField      => InputTextComponent.render(t)
      case ta: InputTextAreaField => InputTextAreaComponent.render(ta)
      case n: InputNumberField    => InputNumberComponent.render(n)
    }
}
