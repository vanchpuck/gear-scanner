package org.izolotov.crawler.v4

import java.net.URL
import java.util.concurrent.{Executors, TimeUnit}

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.HttpClients
import org.izolotov.crawler.parser.product.Product
import org.izolotov.crawler.v4
import org.openqa.selenium.WebDriver
import org.scalatest.FlatSpec

import scala.concurrent.{ExecutionContext, Future}
import CrawlerSpec._

object CrawlerSpec {

  val executor = Executors.newFixedThreadPool(10)
  implicit val ec = ExecutionContext.fromExecutor(executor)

  class DummyScraper {
    def extract(url: String): Unit = {
      println(s"Scraping: ${url}")
    }
  }

  object HostQueueFactory {
    def create(url: String): ScrapingQueue = {
      new FixedDelayQueue(1000L)
    }
  }
}

class CrawlerSpec extends FlatSpec {

  it should "..." in {
    val factory: (String) => ScrapingQueue = HostQueueFactory.create
    val scraper: (String) => Unit = new DummyScraper().extract
    Crawler.crawl(
      Seq("http://example.com/1", "http://google.com/1", "http://example.com/2", "http://example.com", "http://abc.com", "http://google.com/2"),
      new PerHostQueue[Unit](factory).bind(scraper).extract
    )
//      .foreach{
//        f =>
//          f onComplete {
//            r => r.get
//          }
//      }
//    executor.shutdown()
//    executor.awaitTermination(20000L, TimeUnit.MILLISECONDS)
    Thread.sleep(5000L)
//    Crawler.crawl(Seq("url1"), new FixedDelayQueue(1000L).)
  }
}
