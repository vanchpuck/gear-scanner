package org.izolotov.crawler.processor

import org.izolotov.crawler.parser.category.Category
import org.izolotov.crawler.parser.product.Product
import org.izolotov.crawler.{CrawlAttempt, CrawlQueueRecord}

class CategoryProcessor(addToCrawlQueue: (CrawlQueueRecord) => Unit,
                        addToProcessingQueue: (CrawlAttempt[Category]) => Unit,
                        addToDLQueue: (CrawlAttempt[Category]) => Unit = null) extends Processor[Category] {
  override def process(attempt: CrawlAttempt[Category]): CrawlAttempt[Category] = {
    addToProcessingQueue(attempt)
    // TODO add logging
    attempt.document.map{
      doc =>
        doc.productURLs.foreach(url => addToCrawlQueue(CrawlQueueRecord(url.toString, Product.Kind)))
        doc.nextURL.foreach(url => addToCrawlQueue(CrawlQueueRecord(url.toString, Category.Kind)))
    }.getOrElse(Option(addToDLQueue).map(function => function(attempt)))
    attempt
  }
}
