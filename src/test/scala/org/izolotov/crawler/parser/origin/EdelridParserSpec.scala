package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class EdelridParserSpec extends FlatSpec {

  val parser = EdelridParser
  val originalDir = "parser/origin/edelrid"

  it should "extract product names and no next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("edelrid","WO FLATANGER JACKET","http://localhost/media/23332/s/0/49169_054a.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
