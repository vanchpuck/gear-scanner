package org.izolotov.crawler.parser.category

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class EquipParserSpec extends FlatSpec {

  behavior of "Equip product category parser"

  val parser = EquipParser
  val categoryDir = "parser/category/equip-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      new URL("http://localhost/has-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/shop?&mode=search&_folder_id=1344800&p=1")
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
      "http://localhost/shop?mode=product&product_id=2740200",
      "http://localhost/shop?mode=product&product_id=2830200",
      "http://localhost/shop?mode=product&product_id=2908600",
      "http://localhost/shop?mode=product&product_id=2121800",
      "http://localhost/shop?mode=product&product_id=2122000",
      "http://localhost/shop?mode=product&product_id=2824800",
      "http://localhost/shop?mode=product&product_id=2929000",
      "http://localhost/shop?mode=product&product_id=1991200",
      "http://localhost/shop?mode=product&product_id=2121200",
      "http://localhost/shop?mode=product&product_id=2737000",
      "http://localhost/shop?mode=product&product_id=2122600",
      "http://localhost/shop?mode=product&product_id=2756400",
      "http://localhost/shop?mode=product&product_id=2725600"
    )
    assert(category.productURLs.map(_.toString) == expected)
  }

}
