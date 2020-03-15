package org.izolotov.crawler.processor

import org.izolotov.crawler.parser.category.Category
import org.izolotov.crawler.parser.product.Product
import org.izolotov.crawler.{CrawlAttempt, CrawlQueue, CrawlQueueRecord}

class CategoryProcessor(crawlQueue: CrawlQueue, storeToDB: (CrawlAttempt[Category]) => Unit) extends Processor[Category] {
  override def process(attempt: CrawlAttempt[Category]): Unit = {
    storeToDB.apply(attempt)
    // TODO add logging
    attempt.document
      .foreach{
        doc =>
          doc.productURLs.foreach(url => crawlQueue.add(CrawlQueueRecord(url.toString, Product.Kind)))
          doc.nextURL.foreach(url => crawlQueue.add(CrawlQueueRecord(url.toString, Category.Kind)))
      }
  }
}
