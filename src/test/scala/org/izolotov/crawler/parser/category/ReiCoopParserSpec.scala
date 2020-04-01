package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class ReiCoopParserSpec extends FlatSpec {

  behavior of "Rei coop product category parser"

  val parser = ReiCoopParser
  val categoryDir = "parser/category/rei-coop-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next/"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/c/carabiners?page=2")
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
      s"http://localhost/product/130564/camp-usa-hms-compact-carabiner",
      s"http://localhost/product/130249/dmm-ultra-o-screwgate-carabiner-set-set-of-3",
      s"http://localhost/product/130247/dmm-rhino-screwgate-carabiner",
      s"http://localhost/product/117541/metolius-cr-locking-carabiner",
      s"http://localhost/product/115406/wild-country-helium-wiregate-carabiners-package-of-5",
      s"http://localhost/product/115405/wild-country-ascent-lite-locking-carabiner",
      s"http://localhost/product/115404/wild-country-ascent-lite-belay-carabiner",
      s"http://localhost/product/115403/wild-country-ascent-hms-locking-carabiner"
    )
    assert(category.productURLs.map(_.toString) == expected)
  }
}
