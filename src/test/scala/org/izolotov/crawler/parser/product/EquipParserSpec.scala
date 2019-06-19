package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.scalatest.FlatSpec

class EquipParserSpec extends FlatSpec{

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/petzl-sarken-sale.html")
    val actual = EquipParser.parse("http://equip.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://equip.ru/crampon.html",
      "equip.ru",
      "Petzl",
      "Кошки альпинистские SARKEN LLF",
      Seq("Альпинизм, ПромАльп, Арбористика", "Ледовое оборудование", "Кошки"),
      9590,
      Some(13700),
      "Руб."
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/camp-c-14-full-price.html")
    val actual = EquipParser.parse("http://equip.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://equip.ru/crampon.html",
      "equip.ru",
      "C.A.M.P.",
      "Кошки C14- AUTOMATIC",
      Seq("Альпинизм, ПромАльп, Арбористика", "Ледовое оборудование", "Кошки"),
      14990,
      None,
      "Руб."
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/petzl-sarken-no-title.html")
    val actual = EquipParser.parse("http://equip.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://equip.ru/crampon.html",
      "equip.ru",
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
