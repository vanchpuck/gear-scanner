package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class TramontanaParserSpec extends FlatSpec {

  behavior of "Tramontana product category parser"

  val parser = TramontanaParser
  val categoryDir = "parser/category/tramontana-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next/"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/catalog/koshki_2/?PAGEN_2=2")
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
      "http://localhost/product/life_sport_koshki_spike_pro/",
      "http://localhost/product/grivel_koshki_ran_light/"
    )
    assert(category.productURLs.map(_.toString) == expected)
  }
}
