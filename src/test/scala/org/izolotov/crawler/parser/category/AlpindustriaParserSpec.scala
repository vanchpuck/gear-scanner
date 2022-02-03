package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class AlpindustriaParserSpec extends AnyFlatSpec {

  behavior of "Alpindustria product category parser"

  val parser = AlpindustriaParser
  val categoryDir = "parser/category/alpindustria-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/has-not-next.html")
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
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/14743/",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/29431/",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-33403/",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/138761/",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-15136/",
      "http://localhost/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-14601/",
    )
    assert(category.productURLs.map(_.toString) == expected)
  }

}
