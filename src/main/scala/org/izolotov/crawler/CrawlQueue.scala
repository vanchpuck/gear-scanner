package org.izolotov.crawler

trait CrawlQueue {

  def add(message: CrawlQueueRecord): Unit

  def pull[A](numOfMessages: Int = 1): Iterable[CrawlQueueRecord]

}