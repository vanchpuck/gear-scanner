//package org.izolotov.crawler
//
//import java.net.http.HttpClient
//import java.util.Optional
//import java.util.concurrent.Executors
//
//import org.scalatest.{BeforeAndAfter, FlatSpec}
//import org.eclipse.jetty.server.Request
//import org.eclipse.jetty.server.Server
//import org.eclipse.jetty.server.handler.AbstractHandler
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//import org.apache.http.HttpResponse
//import org.apache.http.client.methods.CloseableHttpResponse
//import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
//import org.apache.http.util.EntityUtils
//import org.hamcrest.Matchers.{greaterThan, is}
//import org.izolotov.crawler.timeoutscraper.{FixedDelayModerator, HttpScraper}
//import org.junit.Assert.assertThat
//
//import scala.concurrent.ExecutionContext
//
//class FixedDelayScrapingQueueSpec extends FlatSpec with BeforeAndAfter {
//
//  val PORT = 8091
//
//  val REGULAR_URL = s"http://localhost:${PORT}/regulat.html"
//  val DELAY_URL = s"http://localhost:${PORT}/delay.html"
//  val CONNECTION_REFUSED_URL = s"http://localhost:${PORT + 1}/refused.html"
//  val UNKNOWN_HOST_URL = s"http://___unknown_host_test___/index.html"
//  val MALFORMED_URL = s"^^:*#!?,.localhost:${PORT}/malformed.html"
//
//  val DELAY_IN_MILLIS = 2000L
//  val DUMMY_CONTENT = "Dummy page"
//
//  var server = new Server(PORT);
//  implicit val httpClient: CloseableHttpClient = HttpClients.createDefault;
//
//  class RequestHandler extends AbstractHandler {
//    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
//      val url = baseRequest.getRequestURL.toString
//      if (DELAY_URL == url) try Thread.sleep(DELAY_IN_MILLIS)
//      catch {
//        case exc: InterruptedException =>
//          throw new RuntimeException(exc)
//      }
//      response.setContentType("text/plain;charset=utf-8")
//      response.getWriter.print(DUMMY_CONTENT)
//      response.setStatus(HttpServletResponse.SC_OK)
//      baseRequest.setHandled(true)
//    }
//  }
//
//  before {
//    server.setHandler(new DelayFetcherTest.RequestHandler)
//    server.start()
//  }
//
//  after {
//    server.stop()
//  }
//
//  it should "..." in {
//    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))
////    implicit val scraper: Scraper[CloseableHttpResponse] = new HttpRawScraper()
//    val moderator = new FixedDelayModerator[CloseableHttpResponse]
//    val urls = Seq(REGULAR_URL, REGULAR_URL, REGULAR_URL, REGULAR_URL, REGULAR_URL)
//    for (url <- urls) {
//      val actual = moderator.extract(new HttpScraper(url), 2000L, 1000L)
//      assert(actual.url.equals(url))
//      actual.response.get.close()
////      assertThat(actual.getUrl, is(url))
////      assertThat(EntityUtils.toString(actual.getResponse.get.getEntity), is(DUMMY_CONTENT))
////      assertThat(actual.getException, is(Optional.empty))
////      assertThat(actual.getResponseTime.get, greaterThan(0L))
////      actual.getResponse.get.close()
//    }
//  }
//
////  it should "!!!" in {
////    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)
////    val queue = new FixedDelayScrapingQueue[CloseableHttpResponse]()
////    queue.add(new HttpRawScraper())
////  }
//
//}
