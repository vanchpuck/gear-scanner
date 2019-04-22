package org.izolotov.crawler.parser

import java.nio.charset.Charset

import org.izolotov.crawler.Product
import org.scalatest.FlatSpec

/**
  * Created by izolotov on 22.04.19.
  */
class PlanetaSportParserSpec extends FlatSpec {

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("planeta-sport-parser/zamberlan-denali-sale.html")
    val actual = PlanetaSportParser.parse("http://planeta-sport.ru/denali.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://planeta-sport.ru/denali.html",
      "planeta-sport.ru",
      "Zamberlan",
      "Ботинки 6000 NEW DENALI RR",
      Seq("Обувь", "Мужская", "Ботинки", "Альпинистские"),
      38108,
      Some(54440),
      "Руб."
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("planeta-sport-parser/grivel-rambo-full-price.html")
    val actual = PlanetaSportParser.parse("http://planeta-sport.ru/grivel-rambo.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://planeta-sport.ru/grivel-rambo.html",
      "planeta-sport.ru",
      "Grivel",
      "Кошки Grivel RAMBO 4 COM",
      Seq("Снаряжение", "Альпинизм", "Ледово-снежное снаряжение", "Альпинистские кошки"),
      15900,
      None,
      "Руб."
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("equip-parser/grivel-rambo-no-price.html")
    val actual = PlanetaSportParser.parse("http://planeta-sport.ru/grivel-rambo.html", inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://planeta-sport.ru/grivel-rambo.html",
      "planeta-sport.ru",
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
