package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class SportMarafonParserSpec extends FlatSpec {

  behavior of "Sport Marafon product category parser"

  val parser = SportMarafonParser
  val categoryDir = "parser/category/sport-marafon-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next/"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/catalog/alpinistskie-koshki/?PAGEN_1=2&sort=sort&order=desc")
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
    val expected = Set(
      "http://localhost/catalog/alpinistskie-koshki/elementy-dlya-koshkobotov-krukonogi-air-light/",
      "http://localhost/catalog/alpinistskie-koshki/koshki-camp-tour-nanotech-semi-automatic/",
      "http://localhost/catalog/alpinistskie-koshki/koshki-black-diamond-serac-strap/",
      "http://localhost/catalog/alpinistskie-koshki/koshki-petzl-irvis-hybrid/",
      "http://localhost/catalog/alpinistskie-koshki/elementy-koshkobot-progress-krasnoyarsk/",
      "http://localhost/catalog/alpinistskie-koshki/koshki-camp-alpinist-pro-auto-semi-auto/"
    )
    assert(category.productURLs.map(_.toString).toSet == expected)
  }
}
