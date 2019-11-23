package org.izolotov.crawler.parser.product

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec
import TrekkinnParserSpec._


object TrekkinnParserSpec {
  val ResourceDir = "parser/product/trekkinn-parser"
  val Host = "www.trekkinn.com"
  val Parser = new TrekkinnParser()
}

class TrekkinnParserSpec extends FlatSpec {

  it should "parse product page with no sale price" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      url.toString,
      Host,
      Some("Petzl"),
      Some("Petzl Lynx Leverlock Universel"),
      Seq("Товары для ходьбы", "Кошки"),
      Some(12789.95F),
      None,
      Some("Руб."),
      Some("http://www.trekkinn.com/f/13597/135970293/petzl-lynx-leverlock-universel.jpg"),
      None
    )
    assert(expected == actual)
  }

  it should "parse product page with sale price" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/sale.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      url.toString,
      Host,
      Some("Odlo"),
      Some("Odlo Alliance BL Top L/S"),
      Seq("Мужская одежда", "Футболки"),
      Some(3599F),
      Some(4744.27F),
      Some("Руб."),
      Some("http://www.trekkinn.com/f/13687/136871314/odlo-alliance-bl-top-l-s.jpg"),
      None
    )
    assert(actual == expected)
  }

  it should "not fail if some data required on parsing stage doesn't exist" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/no-data.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = new Product(
      url.toString,
      Host,
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
