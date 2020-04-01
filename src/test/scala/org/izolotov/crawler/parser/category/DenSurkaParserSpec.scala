package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class DenSurkaParserSpec extends FlatSpec {

  behavior of "DenSurka product category parser"

  val parser = DenSurkaParser
  val categoryDir = "parser/category/den-surka-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/products/123?page=3")
  }
  
  it should "not extract next URL if one doesn't exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL == None)
  }

  it should "extract URLs" in {
    val category = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expected = Seq(
      "http://localhost/product/azula_fr_110",
      "http://localhost/product/deva_70_0",
      "http://localhost/product/deva_70_1",
      "http://localhost/product/tungsten_ul_1p",
      "http://localhost/product/zion_haul_bag",
      "http://localhost/product/megamat",
      "http://localhost/product/downmat_ul_winter",
      "http://localhost/product/fortress_3p",
      "http://localhost/product/mercury_65"
    )
    assert(category.productURLs.map(_.toString) == expected)
  }

}
