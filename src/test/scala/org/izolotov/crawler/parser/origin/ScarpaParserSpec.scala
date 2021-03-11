package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class ScarpaParserSpec extends FlatSpec {

  val parser = ScarpaParser
  val originalDir = "parser/origin/scarpa"

  it should "extract product names and no next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("scarpa", "RUSH MID GTX WOMEN'S", "https://www.scarpa.com/media/catalog/product/cache/e93ecbaddbe828dd20275da41d9b72fa/i/p/ipps_rush_mid_gtx-w_ext_blu-fux.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
