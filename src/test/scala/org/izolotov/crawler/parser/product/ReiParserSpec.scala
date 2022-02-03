package org.izolotov.crawler.parser.product

import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.product.ReiParserSpec._
import org.scalatest.flatspec.AnyFlatSpec

object ReiParserSpec {
  val Host = "www.rei.com"
  val Parser = new ReiParser()
}

class ReiParserSpec extends AnyFlatSpec{

  behavior of "REI Parser"

  it should "parse rei-garage page correctly" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/rei-garage-parser/sale.html")
    val actual = Parser.parse(new URL("https://www.rei.com/rei-garage/product.html"), inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "https://www.rei.com/rei-garage/product.html",
      Host,
      Some("Black Diamond"),
      Some("ATC-Guide Belay Device"),
      Seq("Climbing", "Climbing Hardware", "Belay and Rappel Devices"),
      Some(23.73F),
      Some(29.95F),
      Some("USD"),
      Some("https://www.rei.com/media/b15ed92c-0336-4b5a-b90a-b7cb7493a89c")
    )
    assert(expected == actual)
  }

  it should "parse rei-coop page correctly" in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/product/rei-coop-parser/full-price.html")
    val actual = Parser.parse(new URL("https://www.rei.com/product.html"), inStream, Charset.forName("UTF-8"))
    val expected = Product(
      "https://www.rei.com/product.html",
      Host,
      Some("Petzl"),
      Some("Lynx Leverlock Modular Crampons"),
      Seq("Climbing", "Mountaineering Gear", "Crampons", "Ice Climbing Crampons"),
      Some(249.95F),
      None,
      Some("USD"),
      Some("https://www.rei.com/media/8056f03a-c0e5-4f1c-99dd-1a98c1ead983")
    )
    assert(expected == actual)
  }

}
