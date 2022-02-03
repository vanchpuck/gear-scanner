//package org.izolotov.crawler.timeoutscraper
//
//import org.scalatest.{BeforeAndAfter, FlatSpec}
//
//import scala.concurrent.{Await, ExecutionContext, Future}
//import FetchingQueueManagerSpec._
//
//import scala.concurrent.duration.Duration
//
//object FetchingQueueManagerSpec {
//
//  val Delay = 100L
//  val Google = "http://google.com/index.html"
//  val Facebook = "http://facebook.com/index.html"
//
//  def newFetchingQueue(): FetchingQueue = new DummyFetchingQueue()
//
//  class DummyFetchingQueue extends FetchingQueue {
//    override def add[T](url: String, fetcher: String => T)(implicit executionContext: ExecutionContext): Future[T] = {
//      Future{
//        synchronized {
//          Thread.sleep(Delay)
//          fetcher.apply(url)
//        }
//      }
//    }
//  }
//}
//
//class FetchingQueueManagerSpec extends FlatSpec with BeforeAndAfter {
//
//  it should "..." in {
//    import scala.concurrent.ExecutionContext.Implicits.global
//    val manager = new ScrapingCore.FetchingQueueManager(3)(newFetchingQueue)
//    val startTime = System.currentTimeMillis()
//    val futures = Seq(Google, Google, Facebook, Facebook, Google).map{
//      url =>
//        manager.fetch(url,_ => "Ok")
//    }
//    Await.result(Future.sequence(futures), Duration.Inf)
//    val elapsedTime = System.currentTimeMillis() - startTime
//    assert(elapsedTime >= 3 * Delay && elapsedTime < 5 * Delay)
//  }
//
//}
