package org.izolotov.crawler.v3

trait Scraper[Doc, Attempt] {

  def extract[Raw](url: String)(implicit fetcher: Fetcher[Raw], parser: Parsable[Raw, Doc]): Attempt

}
