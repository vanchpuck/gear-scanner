package org.izolotov.crawler.parser.category

import java.nio.charset.Charset

import org.scalatest.FlatSpec

class AlpindustriaParserSpec extends FlatSpec {

  behavior of "Alpindustria product category parser"

  val parser = AlpindustriaParser
  val categoryDir = "parser/category/alpindustria-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      "http://localhost/has-next",
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/has-not-next.html")
  }

  it should "not extract next URL if one doesn't exist" in {
    val category = parser.parse(
      "http://localhost/has-not-next",
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL == None)
  }

  it should "extract URLs" in {
    val category = parser.parse(
      "http://localhost/has-not-next",
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expected = Seq(
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/14743/?id=14742",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/29431/?id=29431",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-33403/?id=339812",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/138761/?id=138761",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-15136/?id=274726",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-14601/?id=270169"
    )
    assert(category.productURLs.map(url => url.get.toString) == expected)
  }

}
