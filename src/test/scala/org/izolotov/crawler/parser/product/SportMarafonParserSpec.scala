package org.izolotov.crawler.parser.product

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class SportMarafonParserSpec extends AnyFlatSpec {

  it should "parse product page with sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/sport-marafon-parser/camp-c-12-sale.html")
    val actual = new SportMarafonParser().parse(new URL("http://sport-marafon.ru/camp-c-12.html"), inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://sport-marafon.ru/camp-c-12.html",
      "sport-marafon.ru",
      Some("Camp"),
      Some("Кошки Camp C12 Universal"),
      Seq("Альпинистское снаряжение", "Ледово-снежное снаряжение", "Альпинистские кошки"),
      Some(9093),
      Some(12990),
      Some("Руб."),
      Some("http://sport-marafon.ru/upload/files/iblock/elements/9838da5b-acd1-11e2-9e97-e83935202582/92043dfb-393e-11e6-80e5-002590d0f723/92043dfb393e11e680e5002590d0f723_66048f5d42a011e680e7002590d0f723.jpg")
    )
    assert(expected == actual)
  }

  it should "parse product page with no sale price" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/sport-marafon-parser/petzl-lynx-full-price.html")
    val actual = new SportMarafonParser().parse(new URL("http://sport-marafon.ru/lynx.html"), inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://sport-marafon.ru/lynx.html",
      "sport-marafon.ru",
      Some("Petzl"),
      Some("Кошки Petzl Lynx"),
      Seq("Альпинистское снаряжение", "Ледово-снежное снаряжение", "Альпинистские кошки"),
      Some(17860),
      None,
      Some("Руб."),
      Some("http://sport-marafon.ru/upload/files/iblock/elements/9838da5b-acd1-11e2-9e97-e83935202582/92043dfb-393e-11e6-80e5-002590d0f723/92043dfb393e11e680e5002590d0f723_66048f5d42a011e680e7002590d0f723.jpg")
    )
    assert(expected == actual)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/equip-parser/petzl-lynx-no-brand.html")
    val actual = new SportMarafonParser().parse(new URL("http://sport-marafon.ru/lynx.html"), inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      "http://sport-marafon.ru/lynx.html",
      "sport-marafon.ru",
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
