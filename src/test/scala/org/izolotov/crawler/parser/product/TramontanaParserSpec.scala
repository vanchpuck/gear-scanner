package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

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
      Some("Black Diamond"),
      Some("Кошки BLACK DIAMOND CYBORG Clip"),
      Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
      Some(17630),
      None,
      Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/tramontana-parser/grivel-air-tech-gsb-sale.html")
    val actual = TramontanaParser.parse("http://tramontana.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://tramontana.ru/crampon.html",
      "tramontana.ru",
      Some("GRIVEL"),
      Some("Кошки GRIVEL AIR TECH GSB"),
      Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
      Some(5355),
      Some(10710),
      Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/tramontana-parser/black-diamond-cyborg-clip-no-data.html")
    val actual = TramontanaParser.parse("http://tramontana.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://tramontana.ru/crampon.html",
      "tramontana.ru",
      None,
      None,
      Seq.empty,
      None,
      None,
      None,
      Some("java.lang.NullPointerException")
    )
    assert(expected == actual)
  }

}
