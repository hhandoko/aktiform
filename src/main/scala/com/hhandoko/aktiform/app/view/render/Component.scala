package com.hhandoko.aktiform.app.view.render

import scala.io.Source
import scala.util.Using

import com.samskivert.mustache.{Mustache, Template}

/** Basic (abstract) trait to define renderable component.
  *
  * All component classes extending this trait must provide path to the template
  * source, relative to `resources/` folder.
  *
  * Components must extend one of two traits:
  *   * RenderComponent, or
  *   * InputComponent
  *
  * Primary difference between them, is that `InputComponent` will escape all
  * HTML characters for all parameters passed to the template.
  *
  * `InputComponent` should be used as the lowest level (i.e. leaf) component
  * which will handle rendering of User's input.
  *
  * `RenderComponent` should be used to build up the page's template, thus it
  * can contain other components as its child to render.
  */
sealed trait Component {

  def escapeHtml: Boolean
  def templatePath: String

  protected lazy val templateSource: String =
    Using.resource(Source.fromResource(templatePath))(_.mkString)

  protected lazy val template: Template =
    Mustache
      .compiler()
      .escapeHTML(escapeHtml)
      .compile(templateSource)
}

trait RenderComponent extends Component {
  final val escapeHtml = false
}

trait InputComponent extends Component {
  final val escapeHtml = true
}
