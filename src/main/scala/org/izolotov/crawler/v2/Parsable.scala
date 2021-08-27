package org.izolotov.crawler.v2

import java.net.URL

trait Parsable[Raw, Doc] {

  def parse(url: String, t: Raw): Doc

}
