//package org.izolotov.crawler.parser.brand
//
//import java.net.URL
//import java.nio.charset.Charset
//
//import org.scalatest.FlatSpec
//
//class GrivelParserSpec extends FlatSpec {
//
//  val parser = GrivelParser
//  val categoryDir = "parser/brand/grivel"
//
//  it should "extract product titles" in {
//    val products = parser.parse(
//      new URL("http://localhost/original"),
//      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/regular.html"),
//      Charset.forName("UTF-8")
//    )
//    val expectedProducts = Seq(
//      "G1",
//      "G10",
//      "G10 wide",
//      "Air Tech",
//      "G12",
//      "Air Tech Light",
//      "Air Tech Light Wide",
//      "Ski Tour",
//      "Ski Tour New Matic",
//      "Haute Route",
//      "Ski Race",
//      "G14",
//      "Rambo 4",
//      "G20 Plus",
//      "G22 Plus",
//      "Racing",
//    )
//    assert(products.toList == expectedProducts)
//  }
//
//}
