package org.izolotov.crawler.parser

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.scalatest.FlatSpec

class AlpindustriaParserSpec extends FlatSpec{

  behavior of "Alpindustria product parser"

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("alpindustria-parser/camp-xlc-nanotech-sale.html")
    val actual = AlpindustriaParser.parse("http://alpindustria.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://alpindustria.ru/crampon.html",
      "alpindustria.ru",
      "camp",
      "Кошки Camp Xlc Nanotech N.B. Step In",
      Seq("Альпинистское снаряжение", "Кошки и снегоступы"),
      7794,
      Some(12990),
      "Руб."
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("alpindustria-parser/petzl-lynx-full-price.html")
    val actual = AlpindustriaParser.parse("http://alpindustria.ru/crampon.html", inStream, Charset.forName("windows-1251"))
    val expected = Product(
      "http://alpindustria.ru/crampon.html",
      "alpindustria.ru",
      "petzl",
      "Кошки Lynx",
      Seq("Альпинистское снаряжение", "Кошки и снегоступы"),
      17590,
      None,
      "Руб."
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("alpindustria-parser/no-data.html")
    val actual = AlpindustriaParser.parse("http://alpindustria.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://alpindustria.ru/crampon.html",
      "alpindustria.ru",
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
