package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.scalatest.FlatSpec
import EquipParserSpec._


object EquipParserSpec {
  val ResourceDir = "parser/product/equip-parser"
  val Host = EquipParser.StoreName
  val Parser = EquipParser
}

class EquipParserSpec extends FlatSpec{

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://product.html",
      Host,
      Some("Petzl"),
      Some("Каска STRATO VENT HI-VIZ"),
      Seq(),
      Some(9350F),
      None,
      Some("Руб."),
      None
    )
    assert(expected == actual)
  }

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/sale.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://product.html",
      Host,
      Some("Petzl"),
      Some("Фонарь REACTIK"),
      Seq(),
      Some(4990F),
      Some(5940F),
      Some("Руб."),
      None
    )
    assert(actual == expected)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/no-data.html")
    val actual = Parser.parse("http://product.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://product.html",
      Host,
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
