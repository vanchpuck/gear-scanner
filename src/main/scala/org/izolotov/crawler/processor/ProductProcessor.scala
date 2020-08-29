package org.izolotov.crawler.processor

import org.izolotov.crawler.parser.product
import org.izolotov.crawler.{CrawlAttempt, CrawlQueueRecord}

class ProductProcessor(addToCrawlQueue: (CrawlQueueRecord) => Unit,
                       addToClassifierQueue: (CrawlAttempt[product.Product]) => Unit,
                       addToDLQueue: (CrawlAttempt[product.Product]) => Unit = null) extends Processor[product.Product] {

  override def process(fetchAttempt: CrawlAttempt[product.Product]): CrawlAttempt[product.Product] = {
    def addToDL = Option(addToDLQueue).map(function => function(fetchAttempt))
    addToClassifierQueue.apply(fetchAttempt)
    fetchAttempt.document.map{
      doc =>
        doc.imageUrl.map(imgUrl => addToCrawlQueue(CrawlQueueRecord(imgUrl, S3Image.Kind)))
        doc.parseError.map(_ => addToDL)
    }.getOrElse(addToDL)
    fetchAttempt
  }
}
