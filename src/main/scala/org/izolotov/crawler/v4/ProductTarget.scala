//package org.izolotov.crawler.v4
//
//import org.izolotov.crawler.parser.product.Product
//
//case class ProductTarget[A](url: String)(implicit scraper: Extractor[A]) extends Target[A] {
//
//  override def extract(): Content[A] = {
//    val a = scraper.extract(url)
//    HttpContent(a.response, a.redirectUrl)
//  }
//}
