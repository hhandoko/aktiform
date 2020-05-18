package com.hhandoko.aktiform.app.view.render.bootstrap

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.junit.runner.RunWith
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.junit.JUnitRunner

import com.hhandoko.aktiform.RandomFixture
import com.hhandoko.aktiform.api.html.input.{FormFieldError, InputTextAreaField}

@RunWith(classOf[JUnitRunner])
class InputTextAreaComponentSpec extends WordSpec with MustMatchers with RandomFixture {

  val browser = new JsoupBrowser()

  "Bootstrap - Text Area input component" when {

    val id    = "test-id"
    val name  = "test"
    val label = "Test Field"

    "rendering field" should {

      "print its ID" in {
        val idToTest = randomText(5)
        val field    = InputTextAreaField(idToTest, name, label)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("label") >> attr("for") mustEqual idToTest
        html >> element("textarea") >> attr("id") mustEqual idToTest
      }

      "print its name" in {
        val nameToTest = randomText(10)
        val field      = InputTextAreaField(id, nameToTest, label)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("textarea") >> attr("name") mustEqual nameToTest
      }

      "print its label" in {
        val labelToTest = randomText(15)
        val field       = InputTextAreaField(id, name, labelToTest)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("label") >> text mustEqual labelToTest
      }

      "not contain optional fields if not provided" in {
        val field = InputTextAreaField(id, name, label)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("textarea") >?> attr("value") mustBe empty
        html >> element("textarea") >?> attr("placeholder") mustBe empty
        html >> element("textarea") >?> attr("required") mustBe empty
        html >> element("textarea") >?> attr("rows") mustBe empty
      }

      "contain value if provided" in {
        val valueToTest = randomText(10)
        val field       = InputTextAreaField(id, name, label, value = Some(valueToTest))

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("textarea") >> attr("value") mustBe valueToTest
      }

      "contain placeholder if provided" in {
        val placeholderToTest = randomText(20)
        val field             = InputTextAreaField(id, name, label, placeholder = Some(placeholderToTest))

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("textarea") >> attr("placeholder") mustBe placeholderToTest
      }

      "contain rows if provided" in {
        val rowToTest = randomInt(20)
        val field     = InputTextAreaField(id, name, label, rows = Some(rowToTest))

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("textarea") >> attr("rows") mustBe rowToTest.toString
      }

      "has required flag if defined" in {
        val field = InputTextAreaField(id, name, label, required = true)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("textarea") >?> attr("required") mustBe defined
      }

      "not contain error class in input given no error" in {
        val field = InputTextAreaField(id, name, label)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >?> element("textarea.is-invalid") mustBe empty
      }

      "contain error class in input given one or more errors" in {
        val error = FormFieldError(randomText(20))
        val field = InputTextAreaField(id, name, label, errors = Seq(error))

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >?> element("textarea.is-invalid") mustBe defined
      }

      "not contain error element given no error" in {
        val field = InputTextAreaField(id, name, label)

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >?> element("div.invalid-feedback") mustBe empty
      }

      "contain error message given one error" in {
        val error = FormFieldError(randomText(20))
        val field = InputTextAreaField(id, name, label, errors = Seq(error))

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        html >> element("div.invalid-feedback") >> text mustBe error.message
      }

      "contain error messages given multiple errors" in {
        val error1 = FormFieldError(randomText(20))
        val error2 = FormFieldError(randomText(25))
        val field  = InputTextAreaField(id, name, label, errors = Seq(error1, error2))

        val raw  = InputTextAreaComponent.render(field)
        val html = browser.parseString(raw)

        val errorList     = (html >> elementList("div.invalid-feedback ul li")).map(_ >> text)
        val containErrors = (contain(error1.message) and contain(error2.message))
        errorList must (containErrors and have size 2)
      }
    }
  }
}
