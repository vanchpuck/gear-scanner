package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class BlackDiamondParserSpec extends AnyFlatSpec {

  val parser = BlackDiamondParser
  val originalDir = "parser/origin/black-diamond"

  it should "extract product names and next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("black diamond", "Saga 40 Jetforce Backpack", "https://blackdiamondequipment.ru/sites/default/files/imagecache/product_list/option-images/681303_BLAK_Saga40_JetForce_web.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == Some(new URL("http://localhost/catalog/packs?page=1")))
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
