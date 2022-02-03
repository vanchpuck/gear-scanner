package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class TrekkinnParserSpec extends AnyFlatSpec {

  behavior of "Backcountry product category parser"

  val parser = TrekkinnParser
  val categoryDir = "parser/category/trekkinn-parser"

  it should "extract URLs" in {
    val category = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expected = Seq(
      "http://localhost/горные/black-diamond-easy-rider-via-ferrata/137116930/p",
      "http://localhost/горные/petzl-kit-corax/137053812/p",
      "http://localhost/горные/petzl-kit-via-ferrata-vertigo-1/136689241/p",
      "http://localhost/горные/petzl-kit-via-ferrata-vertigo-2/136689242/p",
      "http://localhost/горные/ocun-via-ferrata-newton-set/137086581/p",
      "http://localhost/горные/ocun-via-ferrata-webee-pail-set/137086598/p",
      "http://localhost/горные/ocun-via-ferrata-bodyguard-pail-set/137086580/p",
      "http://localhost/горные/ocun-via-ferrata-bodyguard-pail-set/1228815/p",
      "http://localhost/горные/ocun-via-ferrata-webee-set/137086589/p",
      "http://localhost/горные/ocun-climbing-twist-set/137086691/p",
      "http://localhost/горные/ocun-climbing-twist-lady-set/137086692/p",
      "http://localhost/горные/singing-rock-packet-ferrata-ii/136813097/p"
    )
    assert(category.productURLs.map(_.toString) == expected)
  }

}
