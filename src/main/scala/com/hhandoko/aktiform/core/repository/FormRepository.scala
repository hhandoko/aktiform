package com.hhandoko.aktiform.core.repository

import com.hhandoko.aktiform.api.html.input.Form

trait FormRepository {
  def getForm(id: String): Form
}
