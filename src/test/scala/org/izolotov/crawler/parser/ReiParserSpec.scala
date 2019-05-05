package org.izolotov.crawler.parser

import java.nio.charset.Charset

import org.scalatest.FlatSpec
import ReiParserSpec._
import org.izolotov.crawler.Product

object ReiParserSpec {
  val Host = "rei.com"
  val Parser = ReiParser
}

class ReiParserSpec extends FlatSpec{

  behavior of "REI Parser"

  it should "parse rei-garage page correctly" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("rei-garage-parser/sale.html")
    val actual = Parser.parse("https://www.rei.com/rei-garage/product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "https://www.rei.com/rei-garage/product.html",
      Host,
      "Black Diamond",
      "ATC-Guide Belay Device",
      Seq("Climbing", "Climbing Hardware", "Belay and Rappel Devices"),
      23.73F,
      Some(29.95F),
      "USD"
    )
    assert(expected == actual)
  }

  it should "parse rei-coop page correctly" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("rei-coop-parser/full-price.html")
    val actual = Parser.parse("https://www.rei.com/product.html", inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "https://www.rei.com/product.html",
      Host,
      "Petzl",
      "Lynx Leverlock Modular Crampons",
      Seq("Climbing", "Mountaineering Gear", "Crampons", "Ice Climbing Crampons"),
      249.95F,
      None,
      "USD"
    )
    assert(expected == actual)
  }

}
