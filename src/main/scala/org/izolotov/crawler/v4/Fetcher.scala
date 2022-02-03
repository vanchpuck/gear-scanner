package org.izolotov.crawler.v4

import java.net.URL

trait Fetcher[+Raw] {

  def fetch(url: URL): Raw

}
