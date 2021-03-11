package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class MilletParserSpec extends FlatSpec {

  val parser = MilletParser
  val originalDir = "parser/origin/millet"

  it should "extract product names and next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("millet","PROLIGHTER 38+10","https://www.millet-mountain.com/media/catalog/product/cache/6/small_image/289x/9df78eab33525d08d6e5fb8d27136e95/m/i/mis2112-8737-sac-a-dos-38-litres-mixte-bleu-marine-prolighter-38-10.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL.get == new URL("https://www.millet-mountain.com/backpack/ski-touring.html?p=2"))
  }

  it should "not extract next URL if one doesn't exist" in {
    val original = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    assert(original.nextURL == None)
  }

}
