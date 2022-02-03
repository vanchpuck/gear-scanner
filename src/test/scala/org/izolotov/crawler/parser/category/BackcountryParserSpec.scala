package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class BackcountryParserSpec extends AnyFlatSpec {

  behavior of "Backcountry product category parser"

  val parser = BackcountryParser
  val categoryDir = "parser/category/backcountry-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/mountaineering-crampons?page=1")
  }

  it should "not extract next URL if one doesn't exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL == None)
  }

  it should "extract URLs" in {
    val category = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expected = Seq(
      "http://localhost/petzl-antisnow-leopard",
      "http://localhost/petzl-sarken-front-section",
      "http://localhost/grivel-g14-set-alu-spacers-and-bolts",
      "http://localhost/petzl-telemark-toe-bail-wire",
      "http://localhost/kahtoola-connect-gaiter-low",
      "http://localhost/grivel-super-asymmetric-bar",
      "http://localhost/grivel-g10-air-tech-crampon-spare-parts",
    )
    assert(category.productURLs.map(_.toString) == expected)
  }

}
