package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class DmmParserSpec extends FlatSpec {

  val parser = DmmParser
  val originalDir = "parser/origin/dmm"

  it should "extract product names" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("dmm", "Mithril", "https://www.skubank.com/imagestore/product/12060/33/161426/c63_12060_HM101RD-S-Mithril.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
