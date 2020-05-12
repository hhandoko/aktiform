package com.hhandoko.aktiform.api.html.input

sealed trait FormField

final case class InputTextField(id: String, name: String, label: String)
    extends FormField
