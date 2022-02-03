package org.izolotov.crawler.v4

trait Selector[Attempt] {

  def extract(url: String): Attempt

}
