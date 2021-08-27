package org.izolotov.crawler.v2

import org.izolotov.crawler.timeoutscraper.BaseFetcher

trait Fetcher[T <: Target, Raw] {

  def fetch(target: T): Raw

}
