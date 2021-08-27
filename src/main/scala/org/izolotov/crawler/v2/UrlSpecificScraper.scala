//package org.izolotov.crawler.v2
//
//class UrlSpecificScraper[Attempt, Response](rules: (String) => (String) => Response) extends Scraper[Attempt] {
//  override def extract(url: String): Attempt = {
//    rules.apply(url).apply(url)
//  }
//}
