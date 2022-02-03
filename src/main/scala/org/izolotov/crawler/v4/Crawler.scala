package org.izolotov.crawler.v4

import java.util
import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._

object Crawler {
  def crawl[A](dataSource: Iterable[String], scraper: (String) => A): Iterable[A] = {
    dataSource.map(url => scraper(url))
  }
}
