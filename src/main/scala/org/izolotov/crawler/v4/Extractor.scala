package org.izolotov.crawler.v4

trait Extractor[A] {

  def extract(url: String): A

}
