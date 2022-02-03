//package org.izolotov.crawler.v4
//
//import java.net.URL
//
//import org.izolotov.crawler.v3.{Fetcher, Parsable}
//
//// TODO Fetcher should return the HttpResponse interface
//class BiphasicSelector[Raw, Doc](fetcher: Fetcher[Raw], parser: Parser[Raw, Doc]) extends Selector[ScrapingAttempt[Doc]] {
//
//  override def extract(url: String): ScrapingAttempt[Doc] = {
////    val startTime = clock.instant.toEpochMilli
//    val raw = fetcher.fetch(new URL(url))
////    val responseTime = clock.instant.toEpochMilli - startTime
//    val response = parser.parse(new URL(url), raw)
////    ScrapingAttempt(url, 0, Some(0), response)
//    null
//  }
//}
