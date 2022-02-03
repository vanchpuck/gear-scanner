package org.izolotov.crawler.timeoutscraper

trait BaseFetcher[Response] {

  def fetch(url: String): Response

}
