//package org.izolotov.crawler.v4
//
//import java.net.URL
//
//import org.apache.http.HttpResponse
//
////import java.net.http.HttpResponse
//
////object ElasticScraper {
////
////}
//
//class FetcherParserScraper[+Raw, Doc](fetcher: Fetcher[Redirectable[Raw]], parser: Parser[Raw, Redirectable[Doc]]) extends Extractor[Doc] {
//  override def extract(url: String): ScrapingAttempt[Doc] = {
//    // TODO handle exception
//    val pageUrl = new URL(url)
//    val raw = fetcher.fetch(pageUrl)
//    val doc = parser.parse(pageUrl, raw.response)
//    ScrapingAttempt(url, raw.redirectUrl.orElse(doc.redirectUrl), doc.response)
//  }
//}
