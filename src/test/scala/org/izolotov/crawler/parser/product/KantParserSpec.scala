package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.scalatest.FlatSpec

class KantParserSpec extends FlatSpec {

  behavior of "Kant product parser"

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/kant-parser/black-diamond-cyborg-full-price.html")
    val actual = KantParser.parse("http://kant.ru/crampons.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://kant.ru/crampons.html",
      "www.kant.ru",
      Some("BLACK DIAMOND"),
      Some("Кошки BLACK DIAMOND Cyborg Pro Crampons No Color"),
      Seq("Туризм", "Альпинистское снаряжение", "Кошки альпинистские"),
      Some(20590),
      None,
      Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/kant-parser/deuter-aircontact-sale.html")
    val actual = KantParser.parse("http://kant.ru/aircontact.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://kant.ru/aircontact.html",
      "www.kant.ru",
      Some("Deuter"),
      Some("Рюкзак Deuter 2018-19 Aircontact 75 + 10 arctic-navy"),
      Seq("Туризм"),
      Some(14472),
      Some(18090),
      Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/kant-parser/black-diamond-cyborg-no-title.html")
    val actual = KantParser.parse("http://kant.ru/crampons.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://kant.ru/crampons.html",
      "www.kant.ru",
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
