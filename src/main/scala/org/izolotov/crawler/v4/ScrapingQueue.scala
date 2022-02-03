package org.izolotov.crawler.v4

import ScrapingQueue._

import scala.concurrent.Future

object ScrapingQueue {

  private object QueuedScraper {
    def apply[A](queue: ScrapingQueue, scraper: (String) => A): QueuedScraper[A] = {
      new QueuedScraper(queue, scraper)
    }
  }

  class QueuedScraper[A] private (queue: ScrapingQueue, scraper: (String) => A) {

    def extract(url: String): Future[A] = {
      queue.add(url, scraper)
    }
  }

}

abstract class ScrapingQueue {

  def bind[A](scraper: (String) => A) = {
    QueuedScraper[A](this, scraper)
  }

  def add[A](url: String, scraper: (String) => A): Future[A]
}
