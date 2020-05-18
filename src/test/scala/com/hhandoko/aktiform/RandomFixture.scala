package com.hhandoko.aktiform

import scala.util.Random

trait RandomFixture {

  protected def randomText(length: Int): String =
    Random.alphanumeric.take(length).mkString

  protected def randomInt(ceiling: Int): Int =
    Random.nextInt(ceiling)
}
