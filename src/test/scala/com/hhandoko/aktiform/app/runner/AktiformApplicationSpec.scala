package com.hhandoko.aktiform.app.runner

import org.junit.runner.RunWith
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.junit.JUnitRunner
import org.springframework.boot.test.context.SpringBootTest

@RunWith(classOf[JUnitRunner])
@SpringBootTest
class AktiformApplicationSpec extends WordSpec with MustMatchers {

  "Aktiform application runner must load ApplicationContext" in {
    true mustBe true
  }

}
