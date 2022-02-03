package org.izolotov.crawler.v5

import java.net.URL

trait Parser[Raw, Doc] {

  def parse(url: URL, response: Raw): Doc

}
