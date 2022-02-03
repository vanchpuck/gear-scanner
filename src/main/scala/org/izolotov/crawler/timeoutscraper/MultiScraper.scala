//package org.izolotov.crawler.timeoutscraper
//
//import MultiScraper._
//
//import scala.collection.mutable.ArrayBuffer
//
//object MultiScraper {
//  case class ScraperCondition(condition: (String) => Boolean, scraper: Scraper[_, HttpFetchAttempt])
//}
//
//class MultiScraper(default: Scraper[_, HttpFetchAttempt]) extends Scraper[_, HttpFetchAttempt] {
//
//  val scrapers: collection.mutable.Buffer[ScraperCondition] = ArrayBuffer()
//
//  def addScraper(condition: (String) => Boolean, scraper: Scraper[_, HttpFetchAttempt]): Unit = {
//    scrapers.append(ScraperCondition(condition, scraper))
//  }
//
//  override def extract(url: String, fetcher: BaseFetcher[_]): HttpFetchAttempt = {
//    scrapers.find(f => f.condition.apply(url))
//      .map(sc => sc.scraper)
//      .getOrElse(default)
//      .extract(url, fetcher)
//  }
//}
