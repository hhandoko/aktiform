package com.hhandoko.aktiform.api.task

import io.circe.Json

trait Step {
  def run(payload: Json): Json
}
