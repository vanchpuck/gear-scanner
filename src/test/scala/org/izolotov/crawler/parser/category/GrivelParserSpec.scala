//package org.izolotov.crawler.parser.category
//
//import java.net.URL
//import java.nio.charset.Charset
//
//import org.scalatest.FlatSpec
//
//class GrivelParserSpec extends FlatSpec {
//
//  val parser = GrivelParser
//  val categoryDir = "parser/category/grivel-parser"
//
//  it should "extract URLs" in {
//    val category = parser.parse(
//      new URL("http://localhost/urls"),
//      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
//      Charset.forName("UTF-8")
//    )
//    category.productNames.foreach(println)
//    println(category)
////    val expected = Seq(
////      "http://localhost/catalog/product/1800207/",
////      "http://localhost/catalog/product/1757425/",
////      "http://localhost/catalog/product/1621657/",
////      "http://localhost/catalog/product/240849/",
////      "http://localhost/catalog/product/222686/",
////      "http://localhost/catalog/product/449417/",
////      "http://localhost/catalog/product/240920/",
////      "http://localhost/catalog/product/449101/"
////    )
////    assert(category.productURLs.map(_.toString) == expected)
////    category.productURLs.map(_.toString).foreach(println)
//  }
//
//}
