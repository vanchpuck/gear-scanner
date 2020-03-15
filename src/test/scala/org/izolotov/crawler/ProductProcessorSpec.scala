package org.izolotov.crawler

import org.izolotov.crawler.parser.product
import org.izolotov.crawler.parser.product.Product
import org.scalatest.FlatSpec

import scala.collection.mutable.ListBuffer
import ProductProcessorSpec._
import org.izolotov.crawler.processor.{ProductProcessor, S3Image}

object ProductProcessorSpec {
  val Store = "TestStore"
  val URL1 = "http://url1.com"
  val URL2 = "http://url2.com"
  val ImageURL1 = "http://image1.com"
}

class ProductProcessorSpec extends FlatSpec {

  it should "add crawl result to db and image URL (if exist) to crawl queue" in {
    val crawlQueue = new TestQueue()
    val crawlDB: ListBuffer[CrawlAttempt[product.Product]] = new ListBuffer

    val attempts = Seq(
      (URL1, Some(ImageURL1)),
      (URL2, None)
    ).map(pair => CrawlAttempt(pair._1, 0L, None, None, None, Some(Product(pair._1, Store, imageUrl = pair._2))))

    val processor = new ProductProcessor(crawlQueue, crawlDB.+=)
    attempts.foreach(processor.process)

    val queueExpected = Seq(CrawlQueueRecord(ImageURL1, S3Image.Kind))

    assert(attempts.toSet == crawlDB.toSet)
    assert(queueExpected.toList == crawlQueue.pull(10).toList)
  }

}
