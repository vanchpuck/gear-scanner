//package org.izolotov.crawler.v4
//
//class ProxyScraper[Content <: Product](default: Scraper[Content], conditional: Iterable[(String => Boolean, Scraper[Content])] = Seq.empty) extends Scraper[Content] {
//  override def extract(url: String): ScrapingAttempt[Content] = {
//    conditional.find(conditionalScraper => conditionalScraper._1.apply(url))
//      .map(opt => opt._2)
//      .getOrElse(default)
//      .extract(url)
//  }
//}
