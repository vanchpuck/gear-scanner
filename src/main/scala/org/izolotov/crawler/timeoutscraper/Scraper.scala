package org.izolotov.crawler.timeoutscraper

trait Scraper[Response, Attempt] {

  def extract(url: String, fetcher: BaseFetcher[Response]): Attempt

}
