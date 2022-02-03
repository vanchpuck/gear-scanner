package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class ClimbingTechnologyParserSpec extends AnyFlatSpec {

  val parser = ClimbingTechnologyParser
  val originalDir = "parser/origin/climbing-technology"

  it should "extract product names" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )

    val expectedHeadProduct = OriginProduct("climbing technology", "NORTH COULOIR", "https://www.climbingtechnology.com/wp-content/uploads/2017/01/NORTH-COULOIR-HAMMER_3I8050A-255x255.png")
    assert(original.products.head == expectedHeadProduct)
  }

}
