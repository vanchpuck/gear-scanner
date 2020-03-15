package org.izolotov.crawler.processor

import org.izolotov.crawler.parser.product
import org.izolotov.crawler.{CrawlAttempt, CrawlQueue, CrawlQueueRecord}

class ProductProcessor(crawlQueue: CrawlQueue, storeToDB: (CrawlAttempt[product.Product]) => Unit) extends Processor[product.Product] {

  override def process(fetchAttempt: CrawlAttempt[product.Product]): Unit = {
    storeToDB.apply(fetchAttempt)
    for (
      doc <- fetchAttempt.document;
      imgUrl <- doc.imageUrl
    ) yield {
      crawlQueue.add(CrawlQueueRecord(imgUrl, S3Image.Kind))
    }
  }
}
