//package org.izolotov.crawler.timeoutscraper
//
//import java.time.{Clock, Instant, ZoneId}
//
//import org.apache.hadoop.hdfs.client.HdfsClientConfigKeys.HttpClient
//import org.apache.http.impl.client.HttpClients
//import org.izolotov.crawler.parser.product.Product
//import org.izolotov.crawler.timeoutscraper.ScrapingCoreSpec.DummyFetchingQueue
//import org.scalatest.FlatSpec
//
//import scala.concurrent.duration.Duration
//import scala.concurrent.{Await, ExecutionContext, Future}
//
//class HttpScrapingSpec extends FlatSpec {
//
//  class DummyFetchingQueue extends FetchingQueue {
//    override def add[T](url: String, fetcher: String => T)(implicit executionContext: ExecutionContext): Future[T] = {
//      Future{
//        synchronized {
//          Thread.sleep(100L)
//          fetcher.apply(url)
//        }
//      }
//    }
//  }
//  implicit val queueFactory: () => FetchingQueue = () => new DummyFetchingQueue()
//  implicit val httpClient = HttpClients.createDefault()
//  implicit val clock: Clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC"))
//
//  it should "..." in {
//    val core = new ScrapingCore(1)
////    val result = core.extract("https://www.kant.ru/catalog/product/2525683/", new TimeoutFetcher[HttpFetchAttempt](new HttpFetcher(), 1L).fetch, new TestHttpParser().parse)
//    val result = core.extract("https://www.kant.ru/catalog/product/2525683/", new HttpFetcher().fetch, new TestHttpParser().parse)
//
//    try{
//      val data = Await.result(result, Duration.Inf)
//    } catch {
//      case e: Exception => println(e)
//    }
//
//  }
//
////  def f() thr
//
//}
