package com.hhandoko.aktiform.core.syntax

import org.junit.runner.RunWith
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.junit.JUnitRunner

import com.hhandoko.aktiform.RandomFixture

@RunWith(classOf[JUnitRunner])
class JavaPrimitiveExtensionSpec extends WordSpec with MustMatchers with RandomFixture {

  "Java primitives extension" when {

    "converting from Option[Int] to java.lang.Integer" should {
      import JavaPrimitiveExtension.OptionIntToJavaIntegerSyntax

      "convert non empty value (via Option) to its value" in {
        val value = randomInt(100)

        val converted = Option(value).orIntNull

        converted mustEqual Integer.valueOf(value)
      }

      "convert non empty value (via Some) to its value" in {
        val value = randomInt(100)

        val converted = Some(value).orIntNull

        converted mustEqual Integer.valueOf(value)
      }

      "convert empty value to null" in {
        val converted = Option.empty[Int].orIntNull

        converted mustEqual null
      }
    }
  }
}
