//package org.izolotov.crawler.timeoutscraper
//
//import java.util.concurrent.Executors
//
//import org.scalatest.FlatSpec
//
//import scala.concurrent.{Await, ExecutionContext, Future}
//import FixedDelayFetchingQueueSpec._
//
//import scala.concurrent.duration.Duration
//
//object FixedDelayFetchingQueueSpec {
//  val Port = 8092
//  val RegularUrl = s"http://localhost:${Port}/regulat.html"
//}
//
//class FixedDelayFetchingQueueSpec extends FlatSpec {
//
//  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(12))
//
//  object DummyFetcher extends BaseFetcher[String] {
//    override def fetch(url: String): String = {
//      Thread.sleep(10L)
//      "Ok"
//    }
//  }
//
//  it should "respect the delay" in {
//    var timeMarkMillis = 0L
//    val delayMillis = 100L
//    val queue = new FixedDelayScraper(delayMillis)
//    val urls = Seq(RegularUrl, RegularUrl, RegularUrl, RegularUrl, RegularUrl)
//    val futures = urls.map{
//      url =>
//        val future = queue.add(url, DummyFetcher.fetch)
//        future.onComplete{
//          _ =>
//            val currTime = System.currentTimeMillis
//            assert(currTime >= (timeMarkMillis + delayMillis))
//            timeMarkMillis = currTime
//            println("Completed at: " + currTime)
//        }
//        future
//    }
//    Await.result(Future.sequence(futures), Duration.Inf)
//  }
//
//}
