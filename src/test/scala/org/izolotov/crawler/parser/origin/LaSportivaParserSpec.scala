package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class LaSportivaParserSpec extends FlatSpec {

  val parser = LaSportivaParser
  val originalDir = "parser/origin/la-sportiva"

  it should "extract product names and next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProduct = OriginProduct("la sportiva","Genius","https://www.lasportiva.com/media/catalog/product/1/0/10R___1.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=600&width=600&canvas=600:600")

    assert(original.products.head == expectedProduct)
    assert(original.nextURL.get == new URL("https://www.lasportiva.com/en/man/footwear/climbing?p=2"))
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
