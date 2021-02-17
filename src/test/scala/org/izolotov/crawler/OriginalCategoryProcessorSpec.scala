package org.izolotov.crawler

import java.net.URL

import org.izolotov.crawler.parser.origin.{OriginCategory, OriginProduct}
import org.izolotov.crawler.parser.origin.OriginCategory
import org.izolotov.crawler.processor.OriginalCategoryProcessor

//import org.izolotov.crawler.parser.category.Category
import org.izolotov.crawler.parser.product.Product
import org.izolotov.crawler.processor.CategoryProcessor
import org.scalatest.FlatSpec

import scala.collection.mutable.ListBuffer
import CategoryProcessorSpec._

object OriginalCategoryProcessorSpec {
  val Store = "TestStore"
  val CategoryURL1 = "http://category1.com"
  val CategoryURL2 = "http://category2.com"
  val CategoryURL3 = "http://category3.com"
  val ProductURL1 = "http://product1.com"
  val ProductURL2 = "http://product2.com"
}


class OriginalCategoryProcessorSpec extends FlatSpec {

  it should "add add records to the crawl queue and processing queue" in {
    val crawlQueue = new TestQueue[CrawlQueueRecord]()
    val processingQueue = new TestQueue[CrawlAttempt[OriginCategory]]()

    val attempts = Seq(
      (CategoryURL1, CategoryURL2, Seq(OriginProduct("petzl", "lynx", "http://image.com")))
    ).map(category => CrawlAttempt(category._1, 0L, None, None, None, Some(OriginCategory(Some(new URL(category._2)), category._3))))

    val processor = new OriginalCategoryProcessor(crawlQueue.add, processingQueue.add)
    attempts.foreach(processor.process)

    val crawlQueueExpected = Seq(
      CrawlQueueRecord(CategoryURL2, OriginCategory.Kind)
    )

    assert(attempts.toList == processingQueue.pull(10).toList)
    assert(crawlQueueExpected.toList == crawlQueue.pull(10).toList)
  }
}
