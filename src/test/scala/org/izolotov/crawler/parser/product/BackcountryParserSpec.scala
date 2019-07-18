package org.izolotov.crawler.parser.product

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
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/sale.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://product.html",
      Host,
      "Black Diamond",
      "Serac Strap Crampon",
      Seq("Climb", "Ice Climbing", "Ice Climbing Crampons"),
      179.95F,
      Some(184.95F),
      "USD"
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://product.html",
      Host,
      "Grivel",
      "G12 Crampon",
      Seq("Climb", "Ice Climbing", "Ice Climbing Crampons"),
      174.95F,
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
