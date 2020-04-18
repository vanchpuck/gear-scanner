package org.izolotov.crawler.processor

import org.izolotov.crawler.CrawlAttempt

trait Processor[A] {

  def process(fetchAttempt: CrawlAttempt[A]): CrawlAttempt[A]

}
