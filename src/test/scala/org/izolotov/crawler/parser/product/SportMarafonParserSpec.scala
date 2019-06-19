package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.scalatest.FlatSpec

class SportMarafonParserSpec extends FlatSpec {

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/sport-marafon-parser/camp-c-12-sale.html")
    val actual = SportMarafonParser.parse("http://sport-marafon.ru/camp-c-12.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://sport-marafon.ru/camp-c-12.html",
      "sport-marafon.ru",
      "Camp",
      "Кошки Camp C12 Universal",
      Seq("Альпинистское снаряжение", "Ледово-снежное снаряжение", "Альпинистские кошки"),
      9093,
      Some(12990),
      "Руб."
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/sport-marafon-parser/petzl-lynx-full-price.html")
    val actual = SportMarafonParser.parse("http://sport-marafon.ru/lynx.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://sport-marafon.ru/lynx.html",
      "sport-marafon.ru",
      "Petzl",
      "Кошки Petzl Lynx",
      Seq("Альпинистское снаряжение", "Ледово-снежное снаряжение", "Альпинистские кошки"),
      17860,
      None,
      "Руб."
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/petzl-lynx-no-brand.html")
    val actual = SportMarafonParser.parse("http://sport-marafon.ru/lynx.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://sport-marafon.ru/lynx.html",
      "sport-marafon.ru",
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
