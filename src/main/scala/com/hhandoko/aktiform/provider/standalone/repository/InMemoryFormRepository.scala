package com.hhandoko.aktiform.provider.standalone.repository

import com.hhandoko.aktiform.api.html.input.{Form, InputNumberField, InputTextAreaField, InputTextField}
import com.hhandoko.aktiform.core.repository.FormRepository

class InMemoryFormRepository extends FormRepository {

  override def getForm(id: String): Form = {
    val textField      = InputTextField("name", "name", "Name")
    val textAreaField  = InputTextAreaField("notes", "notes", "Notes")
    val numberField    = InputNumberField("age", "age", "Age")
    val transformField = InputTextAreaField("transform", "transform", "Transform Data")
    val fields         = Seq(textField, textAreaField, numberField, transformField)

    Form(s"/forms/${id}", fields)
  }
}
