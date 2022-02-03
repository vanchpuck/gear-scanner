package org.izolotov.crawler.v3

trait Parsable[Raw, Doc] {

  def parse(url: String, t: Raw): Doc

}
