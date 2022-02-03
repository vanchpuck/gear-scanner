package org.izolotov.crawler.v5

trait Scraper[Attempt] {

  def extract(url: String)(implicit processor: Processor[Attempt]): Unit

}
