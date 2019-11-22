package org.izolotov.crawler.parser.product

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec
import DenSurkaParserSpec._

object DenSurkaParserSpec {
  val ResourceDir = "parser/product/den-surka-parser"
  val Host = "www.densurka.ru"
  val Parser = new DenSurkaParser()
}

class DenSurkaParserSpec extends FlatSpec {

  it should "parse product page with no sale price" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/full-price.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://www.densurka.ru/product.html",
      Host,
      Some("BlackDiamond"),
      Some("Vapor"),
      Seq("Снаряжение", "Альпинизм / Скалолазание", "Каски"),
      Some(12990F),
      None,
      Some("Руб."),
      Some("https://www.densurka.ru/sites/default/files/styles/product_display_slide/public/pictures/products/BD/620215_vapor_blzd_sq_20121217101631.jpg?itok=8ctFS3ix"),
      None
    )
    assert(expected == actual)
  }

  it should "parse product page with sale price" in {
    val url = new URL("http", Host, "/product.html")
    val inStream = this.getClass.getClassLoader.getResourceAsStream(s"${ResourceDir}/sale.html")
    val actual = Parser.parse(url, inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "http://www.densurka.ru/product.html",
      Host,
      Some("Sterling Rope"),
      Some("Evolution Velocity"),
      Seq("Снаряжение", "Альпинизм / Скалолазание", "Веревки"),
      Some(6396F),
      Some(15990F),
      Some("Руб."),
      Some("https://www.densurka.ru/sites/default/files/styles/product_display_slide/public/pictures/products/SR/SR-EV060050.jpg?itok=MW2ng4rx"),
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
