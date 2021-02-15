package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class PetzlParserSpec extends FlatSpec {

  val parser = PetzlParser
  val originalDir = "parser/origin/petzl"

  it should "extract product names and next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedNextUrl = new URL("http://localhost/?div=catalog&cat=sport&cat_id=63&num_on_page=15&page=1")
    val expectedHeadProduct = OriginProduct("petzl", "LYNX", new URL("http://localhost/UserFiles/product/m/LYNX_3157_001.jpg").toString)
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
