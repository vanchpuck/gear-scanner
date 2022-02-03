package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class SalewaParserSpec extends AnyFlatSpec {

  val parser = SalewaParser
  val originalDir = "parser/origin/salewa"

  it should "extract product names and next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedNextUrl = new URL("http://localhost/men?p=2")
    val expectedHeadProduct = OriginProduct("salewa", "Alpine Hemp Men's Print T-Shirt", "https://cdn2.salewa.com/media/image/87/24/25/85be447d-6054-4296-8749-e180f3d540fc_salewa_300x300.jpg")
    assert(original.nextURL.get == expectedNextUrl)
    assert(original.products.head == expectedHeadProduct)
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
