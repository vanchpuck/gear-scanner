package org.izolotov.crawler.parser

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.scalatest.FlatSpec
import BackcountryParserSpec._

object BackcountryParserSpec {
  val ResourceDir = "backcountry-parser"
  val Host = "backcountry.com"
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
