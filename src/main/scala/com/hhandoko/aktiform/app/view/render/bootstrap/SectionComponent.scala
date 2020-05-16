package com.hhandoko.aktiform.app.view.render.bootstrap

import java.util.Collections

import com.hhandoko.aktiform.api.html.{Element, Section}
import com.hhandoko.aktiform.app.view.render.RenderComponent

final class SectionComponent(elRender: Element => String) extends RenderComponent {

  val templatePath = "templates/bootstrap/section.mustache"

  def render(section: Section): String =
    template.execute(
      Collections.singletonMap("content", section.content.map(elRender).mkString)
    )
}
