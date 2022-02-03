//package org.izolotov.crawler.timeoutscraper
//
//import org.izolotov.crawler.timeoutscraper.FetchingQueueManagerSpec.Delay
//import org.scalatest.FlatSpec
//
//import scala.concurrent.{Await, ExecutionContext, Future}
//import ScrapingCoreSpec._
//
//import scala.concurrent.duration.Duration
//
//object ScrapingCoreSpec {
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
//
//  class FetchingException extends Exception
//  class ParsingException extends Exception
//
//  implicit val SuccessFetcher: String => String = url => s"${url} - crawled"
//  implicit val FailFetcher: (String) => String = fetched => throw new FetchingException()
//  implicit val SuccessParser: (String) => String = fetched => s"${fetched} - parsed"
//  implicit val FailParser: (String) => String = fetched => throw new ParsingException()
//  implicit val queueFactory: () => FetchingQueue = () => new DummyFetchingQueue()
//
//  val GoogleCom = "http://google.com"
//
//}
//
//class ScrapingCoreSpec extends FlatSpec {
//
//  it should "Successfully fetch and parse" in {
//    val core = new ScrapingCore(2)
//    val f: Future[String] = core.extract(GoogleCom, SuccessFetcher, SuccessParser)
//    val result: String = Await.result(f, Duration.Inf)
//    assert(result == "http://google.com - crawled - parsed")
//  }
//
//  it should "Throw fetching exception if fetching failed" in {
//    val core = new ScrapingCore(2)
//    intercept[ScrapingCore.FetchingException] {
//      Await.result(core.extract(GoogleCom, FailFetcher, SuccessParser), Duration.Inf)
//    }
//  }
//
//}
