package org.izolotov.crawler

import scala.collection.mutable

class TestQueue[A] extends ProcessingQueue[A] {

  private val queue = new mutable.Queue[A]()

  override def add(message: A): Unit = {
    queue += message
  }

  override def pull(numOfMessages: Int): Iterable[A] = {
    for (i <- 0 to numOfMessages; item <- queue.dequeueFirst(_ => true)) yield item
  }
}
