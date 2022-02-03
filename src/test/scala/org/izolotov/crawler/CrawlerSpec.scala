package org.izolotov.crawler

import java.time.Clock
import java.util.concurrent.Executors

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.{Request, Server}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.izolotov.crawler.parser.product.{JsonParser, Product}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization
import org.scalatest.{BeforeAndAfter}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import CrawlerSpec._
import org.izolotov.crawler.processor.Processor
import org.scalatest.flatspec.AnyFlatSpec

object CrawlerSpec {
  val Port = 8082

  val Host1PetzlLynx = s"http://localhost:${Port}/petzl-lynx"
  val Host1BDCyborg = s"http://localhost:${Port}/dlack-diamond-cyborg"
  val Host1PetzlVasak = s"http://localhost:${Port}/petzl-vasak"
  val Host2GrivelRambo = s"http://127.0.0.1:${Port}/grivel-rambo"

  val TestCookie = "test_cookie"
  val ProductName = "name"
  val CookieProductName = "cookie_name"
  val UserAgent = "TestUserAgent"

  implicit val ClockUTC = Clock.systemUTC()
  implicit val FutureSequenceContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
  val CrawlContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  object RequestHandler extends AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
      implicit val formats = DefaultFormats
      val name = if (request.getHeader("Cookie") == s"$TestCookie=true") CookieProductName else ProductName
      val dummyProduct =
        Product(baseRequest.getRequestURL.toString, "store", Some("brand"), Some(name), Seq("category"), Some(1F), None, Some(Currency.Rub.toString), None)
      response.setContentType("text/plain;charset=utf-8")
      response.setStatus(HttpServletResponse.SC_OK)
      response.getWriter.print(Serialization.write(dummyProduct))
      baseRequest.setHandled(true)
    }
  }

  class TestProcessor() extends Processor[Product] {
    private val _processed = new ArrayBuffer[CrawlAttempt[Product]]()
    def processed = _processed
    override def process(fetchAttempt: CrawlAttempt[Product]): CrawlAttempt[Product] = {
      _processed.append(fetchAttempt)
      fetchAttempt
    }
  }
}

class CrawlerSpec extends AnyFlatSpec with BeforeAndAfter {

  var server: Server = null

  before {
    server = new Server(Port)
    server.setHandler(RequestHandler)
    server.start()
  }

  after {
    server.stop()
  }

  it should "crawl different hosts in parallel" in {
    val urls = Seq(
      s"http://localhost:${Port}/product",
      s"http://127.0.0.1:${Port}/product"
    )
    val crawlerConf = CrawlConf(UserAgent, 100L, 1000L)
    val parser = new JsonParser()
    val startTime = System.currentTimeMillis()
    val futures : Seq[Future[Long]] = urls.map(url => Crawler.crawl(url)(ClockUTC, CrawlContext, crawlerConf, parser).map(_ => System.currentTimeMillis() - startTime))
    val result = Await.result(Future.sequence(futures), Duration.Inf)
    val elapsedTime = System.currentTimeMillis() - startTime
    assert(result.reduce((et1, et2) => et1 + et2) > elapsedTime)
  }

  it should "crawl all provided urls" in {
    val urls = Seq(
      s"http://localhost:${Port}/product",
      s"http://localhost:${Port}/product",
      s"http://localhost:${Port}/product",
      s"http://localhost:${Port}/product",
      s"http://127.0.0.1:${Port}/product",
      s"http://127.0.0.1:${Port}/product",
      s"http://127.0.0.1:${Port}/product",
    )

    val crawlerConf = CrawlConf(UserAgent, 0L, 1000L)
    val parser = new JsonParser()
    val futures: Seq[Future[CrawlAttempt[Product]]] = urls.map(url => Crawler.crawl(url)(ClockUTC, CrawlContext, crawlerConf, parser))
    val result = Await.result(Future.sequence(futures), Duration.Inf)
    result.foreach{
      item =>
        assert(item.document.get == Product(item.url, "store", Some("brand"), Some(ProductName), Seq("category"), Some(1F), None, Some(Currency.Rub.toString), None))
    }
  }

  it should "send cookie with request if the config specified" in {
    val urls = Seq(
      s"http://localhost:${Port}/product"
    )
    val url = s"http://localhost:${Port}/product"
    val crawlerConf = CrawlConf(UserAgent, 0L, 1000L, Some(Map(TestCookie -> "true")))
    val parser = new JsonParser()
    val crawled = Await.result(Crawler.crawl(url)(ClockUTC, CrawlContext, crawlerConf, parser), Duration.Inf)
    assert(crawled.document.get == Product(crawled.url, "store", Some("brand"), Some(CookieProductName), Seq("category"), Some(1F), None, Some(Currency.Rub.toString), None))
  }

}
