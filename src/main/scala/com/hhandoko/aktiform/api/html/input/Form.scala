package com.hhandoko.aktiform.api.html.input

import io.circe.Json

import com.hhandoko.aktiform.api.html.Element

final case class Form(target: String, fields: Seq[FormField[_]]) extends Element {
  def fill(data: Map[String, String]): Form = {
    val ff: Seq[FormField[_]] = fields.map { field =>
      data
        .get(field.name)
        .map(field.parse)
        .getOrElse(field)
    }
    this.copy(fields = ff)
  }

  def toJson: Json = {
    val data =
      fields.map {
        case t: InputTextField =>
          (t.name, t.value.fold(Json.Null) { v =>
            Json.fromString(v)
          })

        case ta: InputTextAreaField =>
          (ta.name, ta.value.fold(Json.Null) { v =>
            Json.fromString(v)
          })

        case n: InputNumberField =>
          (n.name, n.value.fold(Json.Null) { v =>
            Json.fromInt(v)
          })
      }

    Json.fromFields {
      Map("data" -> Json.fromFields(data))
    }
  }
}
