package com.hhandoko.aktiform.app.view.render

import com.hhandoko.aktiform.api.html.Page

trait HtmlRender {
  def render(page: Page): String
}
