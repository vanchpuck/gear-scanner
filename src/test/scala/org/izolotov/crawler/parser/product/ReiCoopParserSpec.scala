package org.izolotov.crawler.parser.product

import java.net.URL
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
    val url = new URL("http", Host, "/product.html").toString
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      url,
      Host,
      Some("Petzl"),
      Some("Lynx Leverlock Modular Crampons"),
      Seq("Climbing", "Mountaineering Gear", "Crampons", "Ice Climbing Crampons"),
      Some(249.95F),
      None,
      Some("USD"),
      Some("http://www.rei.com/media/8056f03a-c0e5-4f1c-99dd-1a98c1ead983")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val url = new URL("http", Host, "/product.html").toString
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/no-data.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      url,
      Host,
      None,
      None,
      Seq.empty,
      None,
      None,
      None,
      None,
      Some("java.lang.NullPointerException")
    )
    assert(expected == actual)
  }

}
