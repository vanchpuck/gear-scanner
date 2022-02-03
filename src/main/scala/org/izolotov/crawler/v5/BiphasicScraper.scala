//package org.izolotov.crawler.v5
//
//import java.net.URL
//
//class BiphasicScraper[Raw, Doc](fetcher: Fetcher[Raw], parser: Parser[Raw, Doc]) extends Scraper[ScrapingAttempt[Doc]] {
//
//  override def extract(url: String): ScrapingAttempt[Doc] = {
////    val startTime = clock.instant.toEpochMilli
//    val raw = fetcher.fetch(new URL(url))
////    val responseTime = clock.instant.toEpochMilli - startTime
//    val response = parser.parse(new URL(url), raw)
//    ScrapingAttempt(url, 0, Some(0), response)
//  }
//}
