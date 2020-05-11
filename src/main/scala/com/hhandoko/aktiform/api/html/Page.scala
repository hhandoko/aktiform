package com.hhandoko.aktiform.api.html

final case class Page(els: Seq[Element]) {

  def render(name: String): String = {
    s"""<!doctype html>
       |<html lang="en">
       |<head>
       |</head>
       |<body>
       |    ${els.map(_.render()).mkString}
       |</body>
       |</html>
       |""".stripMargin
  }
}
