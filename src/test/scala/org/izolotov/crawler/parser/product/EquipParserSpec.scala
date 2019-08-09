package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.scalatest.FlatSpec

class EquipParserSpec extends FlatSpec{

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/petzl-sarken-sale.html")
    val actual = EquipParser.parse("http://equip.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://equip.ru/crampon.html",
      "equip.ru",
      Some("Petzl"),
      Some("Кошки альпинистские SARKEN LLF"),
      Seq("Альпинизм, ПромАльп, Арбористика", "Ледовое оборудование", "Кошки"),
      Some(9590),
      Some(13700),
      Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/camp-c-14-full-price.html")
    val actual = EquipParser.parse("http://equip.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://equip.ru/crampon.html",
      "equip.ru",
      Some("C.A.M.P."),
      Some("Кошки C14- AUTOMATIC"),
      Seq("Альпинизм, ПромАльп, Арбористика", "Ледовое оборудование", "Кошки"),
      Some(14990),
      None,
      Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/petzl-sarken-no-title.html")
    val actual = EquipParser.parse("http://equip.ru/crampon.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://equip.ru/crampon.html",
      "equip.ru",
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
