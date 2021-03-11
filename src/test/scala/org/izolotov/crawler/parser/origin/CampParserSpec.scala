package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class CampParserSpec extends FlatSpec {

  val parser = CampParser
  val originalDir = "parser/origin/camp"

  it should "extract product names and no next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("camp","X-Dream","https://www.camp-usa.com/outdoor/wp-content/uploads/2019/11/1-2864-X-DREAM-20-120x120.jpg")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL == None)
  }

}
