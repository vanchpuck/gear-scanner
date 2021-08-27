package org.izolotov.crawler.v2

trait ResponseTrait[+A] {
  def response(): A
}
