//package org.izolotov.crawler.v4
//
//class DefaultBiphasycScraper[Doc, Attempt >: ScrapingAttempt[Doc]]()(implicit clock: Clock) extends Scraper[Doc, Attempt] {
//
//  def extract(url: String)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = {
//    val startTime = clock.instant.toEpochMilli
//    val raw = Try(fetcher.fetch(target))
//    val responseTime = clock.instant.toEpochMilli - startTime
//    val response = raw.map(content => parser.parse(target.url, content))
//    ScrapingAttempt(target.url, startTime, raw.toOption.map(_ => responseTime), response.get)
//  }
//}
