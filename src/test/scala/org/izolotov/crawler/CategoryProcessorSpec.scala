package org.izolotov.crawler

import java.net.URL

import org.izolotov.crawler.parser.product.Product
import org.scalatest.FlatSpec

import scala.collection.mutable.ListBuffer
import CategoryProcessorSpec._
import org.izolotov.crawler.parser.category.Category
import org.izolotov.crawler.processor.CategoryProcessor

object CategoryProcessorSpec {
  val Store = "TestStore"
  val CategoryURL1 = "http://category1.com"
  val CategoryURL2 = "http://category2.com"
  val CategoryURL3 = "http://category3.com"
  val ProductURL1 = "http://product1.com"
  val ProductURL2 = "http://product2.com"
}

class CategoryProcessorSpec extends FlatSpec {

  it should "add crawl result to db, add next category URL and product URLs to crawl queue" in {
    val crawlQueue = new TestQueue()
    val crawlDB: ListBuffer[CrawlAttempt[Category]] = new ListBuffer

    val attempts = Seq(
      (CategoryURL1, Some(new URL(CategoryURL2)), Seq(new URL(ProductURL1), new URL(ProductURL2))),
      (CategoryURL3, None, Seq())
    ).map(category => CrawlAttempt(category._1, 0L, None, None, None, Some(Category(category._2, category._3))))

    val processor = new CategoryProcessor(crawlQueue, crawlDB.+=)
    attempts.foreach(processor.process)

    val queueExpected = Seq(
      CrawlQueueRecord(ProductURL1, Product.Kind),
      CrawlQueueRecord(ProductURL2, Product.Kind),
      CrawlQueueRecord(CategoryURL2, Category.Kind)
    )

    assert(attempts.toSet == crawlDB.toSet)
    assert(queueExpected.toList == crawlQueue.pull(10).toList)
  }

}
