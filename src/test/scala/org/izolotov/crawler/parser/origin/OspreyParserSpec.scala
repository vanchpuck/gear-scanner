package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class OspreyParserSpec extends AnyFlatSpec {

  val parser = OspreyParser
  val originalDir = "parser/origin/osprey"

  it should "extract product names" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("osprey", "Talon Pro 20", "https://cdn.ospreyeurope.com/shop/media/catalog/product/cache/7b88678812a4a0064b128388c3814d36/w/e/web__0124_talon_pro_20_s21_side_carbon_1_2.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
