package com.hhandoko.aktiform.api.task

import scala.reflect.ClassTag

import io.circe.Json

abstract class Step[A](implicit a: ClassTag[A]) {
  val runtimeClassOf = a.runtimeClass
  def run(payload: Json): Json
}

trait IOStep extends Step[String]
