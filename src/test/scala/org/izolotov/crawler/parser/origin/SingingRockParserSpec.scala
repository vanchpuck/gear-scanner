package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class SingingRockParserSpec extends AnyFlatSpec{

  val parser = SingingRockParser
  val originalDir = "parser/origin/singing-rock"

  it should "extract product names and no next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("singing rock", "ICON 9.3", "http://localhost/resize/katalog/250x190/L03/L0380.jpg")

    print(original.products.head)

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
