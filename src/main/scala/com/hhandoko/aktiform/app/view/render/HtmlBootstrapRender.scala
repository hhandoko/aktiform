package com.hhandoko.aktiform.app.view.render

import java.util.{HashMap => JHashMap}
import scala.io.Source

import com.samskivert.mustache.Mustache

import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.api.html.input.{Form, FormField, InputTextField}
import com.hhandoko.aktiform.app.config.ResourcesConfig
import com.hhandoko.aktiform.core.helper.AutoCloseableResource.using

class HtmlBootstrapRender(resourcesConfig: ResourcesConfig) extends HtmlRender {

  private final val PAGE_TEMPLATE = "templates/dynamic.mustache"
  private final val META_TEMPLATE = "templates/section/meta.mustache"
  private final val SCRIPTS_TEMPLATE = "templates/section/scripts.mustache"
  private final val STYLES_TEMPLATE = "templates/section/styles.mustache"

  private final val pageTemplateRaw =
    using(Source.fromResource(PAGE_TEMPLATE))(_.mkString)
  private final val pageTemplate =
    Mustache.compiler().escapeHTML(false).compile(pageTemplateRaw)
  private final val metaTemplateRaw =
    using(Source.fromResource(META_TEMPLATE))(_.mkString)
  private final val metaTemplate =
    Mustache.compiler().compile(metaTemplateRaw)
  private final val scriptsTemplateRaw =
    using(Source.fromResource(SCRIPTS_TEMPLATE))(_.mkString)
  private final val scriptsTemplate =
    Mustache.compiler().compile(scriptsTemplateRaw)
  private final val stylesTemplateRaw =
    using(Source.fromResource(STYLES_TEMPLATE))(_.mkString)
  private final val stylesTemplate =
    Mustache.compiler().compile(stylesTemplateRaw)

  def render(page: Page): String =
    pageTemplate.execute {
      new JHashMap[String, String] {
        this.put("meta", metaTemplate.execute(new JHashMap()))
        this.put("styles", stylesTemplate.execute(new JHashMap[String, AnyRef] {
          this.put("styles", resourcesConfig.bootstrap.styles)
        }))
        this.put("content", page.els.map(render).mkString)
        this.put(
          "scripts",
          scriptsTemplate.execute(new JHashMap[String, AnyRef] {
            this.put("scripts", resourcesConfig.bootstrap.scripts)
            this.put("resources", resourcesConfig)
          })
        )
      }
    }

  def render(el: Element): String = el match {
    case f: Form => render(f)
  }

  def render(field: FormField): String = field match {
    case t: InputTextField => render(t)
  }

  private[this] def render(form: Form): String =
    s"""<form class="needs-validation" action="${form.target}" method="post" novalidate>
       |${form.fields.map(render).mkString}
       |<button class="btn btn-primary btn-lg btn-block" type="submit">Submit</button>
       |</form>
       |""".stripMargin

  private[this] def render(field: InputTextField): String =
    s"""<label for="${field.id}">${field.label}</label>
       |<input id="${field.id}"
       |       name="${field.name}"
       |       class="form-control"
       |       type="text">
       |""".stripMargin
}
