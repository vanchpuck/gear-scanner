package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.scalatest.FlatSpec

class AlpindustriaParserSpec extends FlatSpec{

  behavior of "Alpindustria product parser"

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/alpindustria-parser/camp-xlc-nanotech-sale.html")
    val actual = AlpindustriaParser.parse("http://alpindustria.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://alpindustria.ru/crampon.html",
      "alpindustria.ru",
      Some("camp"),
      Some("Кошки Camp Xlc Nanotech N.B. Step In"),
      Seq("Альпинистское снаряжение", "Кошки и снегоступы"),
      Some(7794),
      Some(12990),
      Some("Руб."),
      Some("http://alpindustria.ru/i/product/l/67867_2.jpg")
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/alpindustria-parser/petzl-lynx-full-price.html")
    val actual = AlpindustriaParser.parse("http://alpindustria.ru/crampon.html", inStream, Charset.forName("windows-1251"))
    val expected = Product(
      "http://alpindustria.ru/crampon.html",
      "alpindustria.ru",
      Some("petzl"),
      Some("Кошки Lynx"),
      Seq("Альпинистское снаряжение", "Кошки и снегоступы"),
      Some(17590),
      None,
      Some("Руб."),
      Some("http://alpindustria.ru/i/product/l/-18852_1.jpg")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/alpindustria-parser/no-data.html")
    val actual = AlpindustriaParser.parse("http://alpindustria.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://alpindustria.ru/crampon.html",
      "alpindustria.ru",
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
