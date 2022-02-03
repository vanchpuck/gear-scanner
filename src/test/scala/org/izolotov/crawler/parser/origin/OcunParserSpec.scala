package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class OcunParserSpec extends AnyFlatSpec {

  val parser = OcunParser
  val originalDir = "parser/origin/ocun"

  it should "extract product names" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("ocun", "Neon", "http://localhost/assets/products/1_400x400/rvz6r9hj5m.04170-neon-1.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
