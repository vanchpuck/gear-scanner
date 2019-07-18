package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.izolotov.crawler.parser.product.ReiCoopParserSpec._
import org.scalatest.FlatSpec

object ReiCoopParserSpec {
  val ResourceDir = "parser/product/rei-coop-parser"
  val Host = "www.rei.com"
  val Parser = ReiCoopParser
}

class ReiCoopParserSpec extends FlatSpec {

  behavior of "Rei coop product parser"

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://product.html",
      Host,
      "Petzl",
      "Lynx Leverlock Modular Crampons",
      Seq("Climbing", "Mountaineering Gear", "Crampons", "Ice Climbing Crampons"),
      249.95F,
      None,
      "USD"
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/no-data.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://product.html",
      Host,
      null,
      null,
      null,
      -1,
      None,
      null,
      Some("java.lang.NullPointerException")
    )
    assert(expected == actual)
  }

}
