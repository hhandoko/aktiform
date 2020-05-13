package com.hhandoko.aktiform.app.view.render

import java.util.{Collections, HashMap => JHashMap}
import scala.io.Source

import com.samskivert.mustache.Mustache

import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.app.config.ResourcesVariantConfig
import com.hhandoko.aktiform.core.helper.AutoCloseableResource.using

final class DynamicPageRender(
  config: ResourcesVariantConfig,
  elRender: Element => String,
) {

  private final val META_TEMPLATE = "templates/section/meta.mustache"
  private final val SCRIPTS_TEMPLATE = "templates/section/scripts.mustache"
  private final val STYLES_TEMPLATE = "templates/section/styles.mustache"
  private final val PAGE_TEMPLATE = "templates/dynamic.mustache"

  private final val metaTemplateRaw =
    using(Source.fromResource(META_TEMPLATE))(_.mkString)
  private final val metaTemplate =
    Mustache.compiler().compile(metaTemplateRaw)
  private final val meta =
    metaTemplate.execute(Collections.EMPTY_MAP)

  private final val scriptsTemplateRaw =
    using(Source.fromResource(SCRIPTS_TEMPLATE))(_.mkString)
  private final val scriptsTemplate =
    Mustache.compiler().compile(scriptsTemplateRaw)
  private final val scripts =
    scriptsTemplate.execute(Collections.singletonMap("scripts", config.scripts))

  private final val stylesTemplateRaw =
    using(Source.fromResource(STYLES_TEMPLATE))(_.mkString)
  private final val stylesTemplate =
    Mustache.compiler().compile(stylesTemplateRaw)
  private final val styles =
    stylesTemplate.execute(Collections.singletonMap("styles", config.styles))

  private final val pageTemplateRaw =
    using(Source.fromResource(PAGE_TEMPLATE))(_.mkString)
  private final val pageTemplate =
    Mustache.compiler().escapeHTML(false).compile(pageTemplateRaw)

  def render(page: Page): String =
    pageTemplate.execute {
      new JHashMap[String, String] {
        this.put("meta", meta)
        this.put("styles", styles)
        this.put("scripts", scripts)
        this.put("content", page.els.map(elRender).mkString)
      }
    }
}
