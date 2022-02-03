package org.izolotov.crawler.timeoutscraper

import java.util.Optional
import java.util.concurrent.Executors

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.util.EntityUtils
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{Request, Server}
import org.hamcrest.Matchers.{allOf, greaterThan, is, lessThanOrEqualTo}
import org.izolotov.crawler.{DelayFetcher, DelayFetcherTest, FetchAttempt}
import org.junit.Assert.assertThat
import org.scalatest.{BeforeAndAfter}
import FixedDelayModeratorSpec._
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

object FixedDelayModeratorSpec {
  val Port = 8091
  val RegularUrl = s"http://localhost:${Port}/regulat.html"
  val DelayUrl = s"http://localhost:${Port}/delay.html"
  val DelayInMillis = 2000L
}

class FixedDelayModeratorSpec extends AnyFlatSpec with BeforeAndAfter {

  var server = new Server(Port)
  implicit val httpClient: CloseableHttpClient = HttpClients.createDefault
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  class RequestHandler extends DummyRequestHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
      val url = baseRequest.getRequestURL.toString
      if (DelayUrl == url) try Thread.sleep(DelayInMillis)
      super.handle(target, baseRequest, request, response)
    }
  }

  before {
    server.setHandler(new DelayFetcherTest.RequestHandler)
    server.start()
  }

  after {
    server.stop()
  }

//  it should "fetch regular pages" in {
//    val moderator = new FixedDelayModerator(200L)//[CloseableHttpResponse]
//    val urls = Seq(RegularUrl, RegularUrl, RegularUrl, RegularUrl, RegularUrl)
//    for (url <- urls) {
//      val actual = moderator.extract(url, new HttpFetcher())
//      assert(actual.url == url)
//      assert(EntityUtils.toString(actual.response.get.getEntity) == DummyRequestHandler.DummyContent)
//      assert(actual.exception == None)
//      assert(actual.responseTime.get > 0L)
//      actual.response.get.close()
//    }
//  }
//
//  it should "have the correct response time value" in {
//    val fetcher = new DelayFetcher(httpClient)
//    val startTime = System.currentTimeMillis
//    val response = fetcher.fetch(DelayUrl)
//    val elapsedTime = System.currentTimeMillis - startTime
//    response.getResponse.get.close()
//    assert(response.getResponseTime.get >= elapsedTime || response.getResponseTime.get > DelayInMillis)
//  }
//
//  it should "have no delay before the first fetching" in {
//    val delay = 3000L
//    val moderator = new FixedDelayModerator(delay)//[CloseableHttpResponse]
//    val startTime = System.currentTimeMillis
//    val response = moderator.extract(DelayUrl, new HttpFetcher())
//    val elapsedTime = System.currentTimeMillis - startTime
//    response.response.get.close()
//    assert(elapsedTime < delay)
//  }
//
//  it should "respect a delay between " in {
//    val delay = 100L
//    val moderator = new FixedDelayModerator(delay)
//    val urls = Seq(RegularUrl, RegularUrl, RegularUrl)
//    val startTime = System.currentTimeMillis
//    val responseTimesSum = urls.map{
//      url =>
//        val response = moderator.extract(url, new HttpFetcher())
//        response.response.get.close()
//        println(response.responseTime.get)
//        response.responseTime.get
//    }.reduce((t1, t2) => t1 + t2)
//    val elapsedTime = System.currentTimeMillis - startTime
//    assert(elapsedTime > (responseTimesSum + (urls.size - 1) * delay))
//  }
//
//  it should "have no additional delay before fetching when the interval greater that the delay value" in {
//    val delay = 1000L
//    val moderator = new FixedDelayModerator(delay)//[CloseableHttpResponse]
//    var response = moderator.extract(RegularUrl, new HttpFetcher())
//    response.response.get.close()
//    Thread.sleep(delay + 100L)
//    val startTime = System.currentTimeMillis
//    response = moderator.extract(RegularUrl, new HttpFetcher())
//    val elapsedTime = System.currentTimeMillis - startTime
//    response.response.get.close()
//    assert(elapsedTime < delay)
//  }

  // !!! MOVE THE RESPONSE TIME PROPERTY TO AN UPPER ABSTRACTION LEVEL !!!

//  it should "stop scraping on timeout" in {
//    val timeout = 500L
//    val delay = 500L
//    val fetcher = new DelayFetcher(httpClient)
//    assertFalse("The non delayed page fetching must not be terminated by timeout", getFetchException(fetcher, REGULAR_URL, delay, timeout).isPresent)
//    assertThat("The delayed page fetching must be terminated by timeout", getFetchException(fetcher, DELAY_URL, delay, timeout).get, instanceOf(classOf[TimeoutException]))
//    assertFalse("The timeout occurred must not affect the following fetching", getFetchException(fetcher, REGULAR_URL, delay, timeout).isPresent)
//  }

}
