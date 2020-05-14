package com.hhandoko.aktiform.app.view.render

import scala.io.Source

import com.samskivert.mustache.{Mustache, Template}

import com.hhandoko.aktiform.core.helper.AutoCloseableResource.using

trait Component {

  def templatePath: String

  protected lazy val templateRaw: String =
    using(Source.fromResource(templatePath))(_.mkString)

  protected lazy val template: Template =
    Mustache
      .compiler()
      .compile(templateRaw)
}
