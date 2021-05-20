package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class PetzlParserSpec extends FlatSpec {

  val parser = PetzlParser
  val originalDir = "parser/origin/petzl"

  it should "extract product names" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedHeadProduct = OriginProduct("petzl", "LEOPARD LLF", "https://www.petzl.com/sfc/servlet.shepherd/version/download/068w0000002DQKzAAO")
    assert(original.nextURL == None)
    assert(original.products.head == expectedHeadProduct)
  }

}
