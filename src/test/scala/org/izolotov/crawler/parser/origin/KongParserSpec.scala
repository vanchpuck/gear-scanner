package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class KongParserSpec extends AnyFlatSpec {

  val parser = KongParser
  val originalDir = "parser/origin/kong"

  it should "extract product names and no next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("kong", "Antipot", "http://localhost/media/Component/CatalogProduct/p337-antipot/images/antipot.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
