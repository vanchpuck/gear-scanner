package org.izolotov.crawler.v5

import java.net.URL

trait Fetcher[Raw] {

  def fetch(url: URL): Raw

}
