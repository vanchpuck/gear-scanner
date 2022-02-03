//package org.izolotov.crawler
//
//import java.util.concurrent.Executors
//
//import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
//import org.apache.http.client.methods.CloseableHttpResponse
//import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
//import org.eclipse.jetty.server.{Request, Server}
//import org.eclipse.jetty.server.handler.AbstractHandler
//import org.izolotov.crawler.timeoutscraper.{HttpScraperSpec, TimeoutScraper}
//import org.scalatest.{BeforeAndAfter, FlatSpec}
//
//import scala.concurrent.ExecutionContext
//
//class TimeoutSpec extends FlatSpec with BeforeAndAfter{
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
//  it should ",,," in {
//    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))
//    val crawler = new TimeoutScraper[CloseableHttpResponse](new HttpScraper(None), 10000L)
//    print(crawler.extract(REGULAR_URL))
//  }
//}
