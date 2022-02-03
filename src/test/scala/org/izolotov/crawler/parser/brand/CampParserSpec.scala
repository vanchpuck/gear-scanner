//package org.izolotov.crawler.parser.brand
//
//import java.net.URL
//import java.nio.charset.Charset
//
//import org.scalatest.FlatSpec
//
//class CampParserSpec extends FlatSpec {
//
//  val parser = CampParser
//  val categoryDir = "parser/brand/camp"
//
//  it should "extract product titles" in {
//    val products = parser.parse(
//      new URL("http://localhost/original"),
//      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/regular.html"),
//      Charset.forName("UTF-8")
//    )
//    val expectedProducts = Seq(
//      "blade runner",
//      "blade runner alpine",
//      "alpinist tech",
//      "alpinist pro",
//      "alpinist universal",
//      "alpinist auto / semi-auto",
//      "stalker universal",
//      "stalker semi-auto",
//      "skimo total race",
//      "skimo race",
//      "skimo tour",
//      "skimo nanotech",
//      "xlc nanotech semi-auto",
//      "xlc nanotech automatic",
//      "xlc 490 universal",
//      "xlc 470 semi-auto",
//      "xlc 390 automatic",
//      "tour nanotech automatic",
//      "frost"
//    )
//    assert(products.toList == expectedProducts)
//  }
//}
