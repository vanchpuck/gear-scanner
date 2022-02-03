package org.izolotov.crawler.v3

trait Fetcher[Raw] {

  def fetch(url: String): Raw

}
