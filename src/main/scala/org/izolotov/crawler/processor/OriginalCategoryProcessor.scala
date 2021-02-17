package org.izolotov.crawler.processor

import org.izolotov.crawler.parser.origin.OriginCategory
import org.izolotov.crawler.{CrawlAttempt, CrawlQueueRecord}

class OriginalCategoryProcessor(addToCrawlQueue: (CrawlQueueRecord) => Unit,
                                storeToDB: (CrawlAttempt[OriginCategory]) => Unit,
                                addToDLQueue: (CrawlAttempt[OriginCategory]) => Unit = null) extends Processor[OriginCategory] {

  override def process(attempt: CrawlAttempt[OriginCategory]): CrawlAttempt[OriginCategory] = {
    storeToDB.apply(attempt)
    // TODO add logging
    attempt.document.map{
      doc =>
        doc.nextURL.foreach(url => addToCrawlQueue(CrawlQueueRecord(url.toString, OriginCategory.Kind)))
    }.getOrElse(Option(addToDLQueue).map(function => function(attempt)))
    attempt
  }
}
