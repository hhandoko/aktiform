package com.hhandoko.aktiform.app.view.render

import java.util.{Collections, HashMap => JHashMap}
import scala.io.Source

import com.samskivert.mustache.Mustache

import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.app.config.ResourcesVariantConfig
import com.hhandoko.aktiform.core.helper.AutoCloseableResource.using

final class DynamicPageRender(
    config: ResourcesVariantConfig,
    elRender: Element => String
) {

  private final val META_TEMPLATE = "templates/section/meta.mustache"
  private final val SCRIPTS_TEMPLATE = "templates/section/scripts.mustache"
  private final val STYLES_TEMPLATE = "templates/section/styles.mustache"
  private final val PAGE_TEMPLATE = "templates/dynamic.mustache"

  private final val meta =
    template(META_TEMPLATE)(Collections.EMPTY_MAP)

  private final val scripts =
    template(SCRIPTS_TEMPLATE) {
      Collections.singletonMap("scripts", config.scripts)
    }

  private final val styles =
    template(STYLES_TEMPLATE) {
      Collections.singletonMap("styles", config.styles)
    }

  private final val pageTemplate =
    template(PAGE_TEMPLATE, escapeHtml = false) _

  def render(page: Page): String =
    pageTemplate {
      new JHashMap[String, String] {
        this.put("meta", meta)
        this.put("styles", styles)
        this.put("scripts", scripts)
        this.put("content", page.els.map(elRender).mkString)
      }
    }

  private[this] def template(path: String, escapeHtml: Boolean = true)(
      context: AnyRef
  ): String = {
    val rawTemplate = using(Source.fromResource(path))(_.mkString)

    Mustache
      .compiler()
      .escapeHTML(escapeHtml)
      .compile(rawTemplate)
      .execute(context)
  }
}
