package com.hhandoko.aktiform.app.view.render.bootstrap

import scala.util.Random

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.junit.runner.RunWith
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.junit.JUnitRunner

import com.hhandoko.aktiform.api.html.input.{FormFieldError, InputTextField}

@RunWith(classOf[JUnitRunner])
class InputTextComponentSpec extends WordSpec with MustMatchers {

  val browser = new JsoupBrowser()

  "Bootstrap - Text input component" when {

    val id    = "test-id"
    val name  = "test"
    val label = "Test Field"

    "rendering field" should {

      "print its ID" in {
        val idToTest = randomText(5)
        val field    = InputTextField(idToTest, name, label)

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("label") >> attr("for") mustEqual idToTest
        html >> element("input") >> attr("id") mustEqual idToTest
      }

      "print its name" in {
        val nameToTest = randomText(10)
        val field      = InputTextField(id, nameToTest, label)

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("input") >> attr("name") mustEqual nameToTest
      }

      "print its label" in {
        val labelToTest = randomText(15)
        val field       = InputTextField(id, name, labelToTest)

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("label") >> text mustEqual labelToTest
      }

      "not contain optional fields if not provided" in {
        val field = InputTextField(id, name, label)

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("input") >?> attr("value") mustBe empty
        html >> element("input") >?> attr("placeholder") mustBe empty
        html >> element("input") >?> attr("required") mustBe empty
      }

      "contain value if provided" in {
        val valueToTest = randomText(10)
        val field       = InputTextField(id, name, label, value = Some(valueToTest))

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("input") >> attr("value") mustBe valueToTest
      }

      "contain placeholder if provided" in {
        val placeholderToTest = randomText(20)
        val field             = InputTextField(id, name, label, placeholder = Some(placeholderToTest))

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("input") >> attr("placeholder") mustBe placeholderToTest
      }

      "has required flag if defined" in {
        val field = InputTextField(id, name, label, required = true)

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("input") >?> attr("required") mustBe defined
      }

      "not contain error element given no error" in {
        val field = InputTextField(id, name, label)

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >?> element("div.invalid-feedback") mustBe empty
      }

      "contain error message given one error" in {
        val error = FormFieldError(randomText(20))
        val field = InputTextField(id, name, label, errors = Seq(error))

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("div.invalid-feedback") >> text mustBe error.message
      }

      "contain error messages given multiple errors" in {
        val error1 = FormFieldError(randomText(20))
        val error2 = FormFieldError(randomText(25))
        val field  = InputTextField(id, name, label, errors = Seq(error1, error2))

        val raw  = InputTextComponent.render(field)
        val html = browser.parseString(raw)

        val errorList     = (html >> elementList("div.invalid-feedback ul li")).map(_ >> text)
        val containErrors = (contain(error1.message) and contain(error2.message))
        errorList must (containErrors and have size 2)
      }
    }
  }

  private[this] def randomText(length: Int): String =
    Random.alphanumeric.take(length).mkString
}
