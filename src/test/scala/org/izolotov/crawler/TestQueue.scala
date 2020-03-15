package org.izolotov.crawler

import scala.collection.mutable

class TestQueue extends CrawlQueue {

  private val queue = new mutable.Queue[CrawlQueueRecord]()

  override def add(message: CrawlQueueRecord): Unit = {
    queue += message
  }

  override def pull[A](numOfMessages: Int): Iterable[CrawlQueueRecord] = {
    for (i <- 0 to numOfMessages; item <- queue.dequeueFirst(_ => true)) yield item
  }
}
