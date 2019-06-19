package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.scalatest.FlatSpec

// TODO handle the case where there is no item in stock
class TramontanaParserSpec extends FlatSpec {

  behavior of "Tramontana product parser"

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/tramontana-parser/black-diamond-cyborg-clip-full-price.html")
    val actual = TramontanaParser.parse("http://tramontana.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://tramontana.ru/crampon.html",
      "tramontana.ru",
      "Black Diamond",
      "Кошки BLACK DIAMOND CYBORG Clip",
      Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
      17630,
      None,
      "Руб."
    )
    assert(expected == actual)
  }

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/tramontana-parser/grivel-air-tech-gsb-sale.html")
    val actual = TramontanaParser.parse("http://tramontana.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://tramontana.ru/crampon.html",
      "tramontana.ru",
      "GRIVEL",
      "Кошки GRIVEL AIR TECH GSB",
      Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
      5355,
      Some(10710),
      "Руб."
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/tramontana-parser/black-diamond-cyborg-clip-no-data.html")
    val actual = TramontanaParser.parse("http://tramontana.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://tramontana.ru/crampon.html",
      "tramontana.ru",
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
