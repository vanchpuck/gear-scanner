package org.izolotov.crawler.timeoutscraper

trait PostFetcher[T, V] {

  def postFetch(fetched: T): V

}
