//package org.izolotov.crawler
//
//import org.izolotov.crawler.parser.product
//import org.izolotov.crawler.parser.product.Product
//
//import scala.collection.mutable.ListBuffer
//import ProductProcessorSpec._
//import org.izolotov.crawler.processor.{ProductProcessor, S3Image}
//import org.scalatest.flatspec.AnyFlatSpec
//
//object ProductProcessorSpec {
//  val Store = "TestStore"
//  val URL1 = "http://url1.com"
//  val URL2 = "http://url2.com"
//  val ImageURL1 = "http://image1.com"
//  val ErrorMessage = "Some error message"
//}
//
//class ProductProcessorSpec extends AnyFlatSpec {
//
//  it should "add crawl result to db and image URL (if exist) to crawl queue" in {
//    val crawlQueue = new TestQueue[CrawlQueueRecord]()
//    val deadLetterQueue = new TestQueue[CrawlAttempt[product.Product]]()
//    val crawlDB: ListBuffer[CrawlAttempt[product.Product]] = new ListBuffer
//
//    val attempts = Seq(
//      (URL1, Some(ImageURL1), None),
//      (URL2, None, Some(ErrorMessage))
//    ).map(args => CrawlAttempt(args._1, 0L, None, None, None, Some(Product(args._1, Store, imageUrl = args._2, parseError = args._3))))
//
//    val processor = new ProductProcessor(crawlQueue.add, crawlDB.+=, deadLetterQueue.add)
//    attempts.foreach(processor.process)
//
//    val queueExpected = Seq(CrawlQueueRecord(ImageURL1, S3Image.Kind))
//    val dlQueueExpected = Seq(CrawlAttempt(URL2, 0L, None, None, None, Some(Product(URL2, Store, parseError = Some(ErrorMessage)))))
//
//    assert(attempts.toSet == crawlDB.toSet)
//    assert(queueExpected.toList == crawlQueue.pull(10).toList)
//    assert(dlQueueExpected.toList == deadLetterQueue.pull(10).toList)
//  }
//
//}
