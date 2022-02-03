package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.flatspec.AnyFlatSpec

class KantParserSpec extends AnyFlatSpec {

  behavior of "Kant product category parser"

  val parser = KantParser
  val categoryDir = "parser/category/kant-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next/"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/has-next/?PAGEN_1=2")
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
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expected = Seq(
      "http://localhost/catalog/product/1800207/",
      "http://localhost/catalog/product/1757425/",
      "http://localhost/catalog/product/1621657/",
      "http://localhost/catalog/product/240849/",
      "http://localhost/catalog/product/222686/",
      "http://localhost/catalog/product/449417/",
      "http://localhost/catalog/product/240920/",
      "http://localhost/catalog/product/449101/"
    )
    assert(category.productURLs.map(_.toString) == expected)
  }
}
