package com.hhandoko.aktiform.app.view.render

import java.util.{Collections, HashMap => JHashMap}
import scala.io.Source

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Mustache.TemplateLoader
import com.typesafe.scalalogging.LazyLogging

import com.hhandoko.aktiform.api.html.{Element, Page}
import com.hhandoko.aktiform.app.config.ResourcesVariantConfig
import com.hhandoko.aktiform.core.helper.AutoCloseableResource.using

/** Page renderer using dynamic master template.
  *
  * @param config CSS variant resources configuration.
  * @param elRender Element renderer.
  */
final class DynamicPageRender(
    config: ResourcesVariantConfig,
    elRender: Element => String
) extends LazyLogging {

  /** Base template to be executed later.
    *
    * This template will resolve styling and script-related segments, and keep
    * other segments (e.g. `{{content}}`) to be updated later via another
    * Mustache template execution.
    */
  private final lazy val templateBase = {
    logger.debug(
      "[init#templateBase] Loading precached raw base template for dynamic pages"
    )

    val master = "templates/dynamic.mustache"
    val section = (name: String) => s"templates/${name}.mustache"
    val loader: TemplateLoader = (name: String) => {
      Source.fromResource(section(name)).reader()
    }
    val raw = using(Source.fromResource(master))(_.mkString)
    val context =
      new JHashMap[String, AnyRef] {
        this.put("meta", Collections.EMPTY_MAP)
        this.put("styles", config.styles)
        this.put("scripts", config.scripts)

        // Rewrap `content` segment to be executed later.
        //
        // Default behaviour when values are missing: `strict=true` with throw
        // exception while `strict=false` will replace it with `false`.
        this.put("content", "{{content}}")
      }

    Mustache
      .compiler()
      .withLoader(loader)
      .compile(raw)
      .execute(context)
  }

  private final lazy val template = {
    logger.debug(
      "[init#template] Loading executable template for dynamic pages"
    )

    Mustache
      .compiler()
      .escapeHTML(false)
      .compile(templateBase)
  }

  def render(page: Page): String =
    template.execute(
      Collections.singletonMap("content", page.els.map(elRender).mkString)
    )
}
