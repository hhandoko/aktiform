package com.hhandoko.aktiform.api.html.input

import com.hhandoko.aktiform.api.html.Element

final case class Form(target: String, fields: Seq[FormField[_]]) extends Element
