package org.izolotov.crawler.timeoutscraper

trait CrawlProcess[Response, Document, Result] {

  def crawl(url: String, fetcher: BaseFetcher[Response], parser: BaseParser[Response, Document]): Result

}
