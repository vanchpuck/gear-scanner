package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.izolotov.crawler.parser.product.ReiGarageParserSpec._
import org.scalatest.FlatSpec

object ReiGarageParserSpec {
  val ResourceDir = "parser/product/rei-garage-parser"
  val Host = "rei.com"
  val Parser = ReiGarageParser
}

class ReiGarageParserSpec extends FlatSpec {

  behavior of "Rei garage product parser"

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/sale.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://product.html",
      Host,
      "Black Diamond",
      "ATC-Guide Belay Device",
      Seq("Climbing", "Climbing Hardware", "Belay and Rappel Devices"),
      23.73F,
      Some(29.95F),
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
      Some("java.util.NoSuchElementException: key not found: cleanTitle")
    )
    assert(expected == actual)
  }

}
