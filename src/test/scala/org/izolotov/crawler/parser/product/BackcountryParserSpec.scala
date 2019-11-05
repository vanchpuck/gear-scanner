package org.izolotov.crawler.parser.product

import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.product.BackcountryParserSpec._
import org.scalatest.FlatSpec

object BackcountryParserSpec {
  val ResourceDir = "parser/product/backcountry-parser"
  val Host = "www.backcountry.com"
  val Parser = BackcountryParser
}

class BackcountryParserSpec extends FlatSpec {

  behavior of "Backcountry product parser"

  it should "parse product page with sale price" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/sale.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      url.toString,
      Host,
      Some("Black Diamond"),
      Some("Serac Strap Crampon"),
      Seq("Climb", "Ice Climbing", "Ice Climbing Crampons"),
      Some(179.95F),
      Some(184.95F),
      Some("USD"),
      Some("http://content.backcountry.com/images/items/1200/BLD/BLD00EV/POL.jpg")
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      url.toString,
      Host,
      Some("Grivel"),
      Some("G12 Crampon"),
      Seq("Climb", "Ice Climbing", "Ice Climbing Crampons"),
      Some(174.95F),
      None,
      Some("USD"),
      Some("http://content.backcountry.com/images/items/1200/GRV/GRV0050/COM.jpg")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/no-data.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      url.toString,
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
