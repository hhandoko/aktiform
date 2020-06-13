package com.hhandoko.aktiform.api.task

import scala.reflect.ClassTag

import io.circe.Json

abstract class Step[A, B](implicit a: ClassTag[A]) {
  val runtimeClassOf = a.runtimeClass

  def decode(in: A): Json
  def encode(in: Json): B

  def map(payload: Json): Json
  def run(payload: A): B
}

trait IOStep[A, B] extends Step[A, B]
