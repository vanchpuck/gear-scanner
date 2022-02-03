package org.izolotov.crawler.v2

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.{Request, Server, ServerConnector}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.izolotov.crawler.DelayFetcherTest
import org.scalatest.{BeforeAndAfter}
import ChromeSeleniumFetcherSpec._
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import ChromeSeleniumFetcherSpec._
import org.apache.http.HttpStatus
import org.scalatest.flatspec.AnyFlatSpec

object ChromeSeleniumFetcherSpec {

  val Port = 8085
  val HelloWorld = "Hello world!"

  class RequestHandler extends AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {

      response.setContentType("text/plain;charset=utf-8")
      response.getWriter.print(HelloWorld)
      response.setStatus(HttpServletResponse.SC_OK)
      baseRequest.setHandled(true)
    }
  }
}

class ChromeSeleniumFetcherSpec extends AnyFlatSpec with BeforeAndAfter {

  var server = new Server(Port)
  implicit val httpClient: CloseableHttpClient = HttpClients.createDefault

  before {
    server.setHandler(new RequestHandler())
    server.start()
  }

  after {
    server.stop()
  }

  it should "successfully fetch simple web page" in {
    val fetcher = new ChromeSeleniumFetcher("http://127.0.0.1:4444/wd/hub", "http://localhost:9222/json")
    val attempt = fetcher.fetch(SeleniumWebPage(s"http://localhost:${Port}/"))
    try {
      assert(attempt.responseData.getStatus == HttpStatus.SC_OK)
      assert(attempt.driver.getPageSource == "<html><head></head><body><pre style=\"word-wrap: break-word; white-space: pre-wrap;\">Hello world!</pre></body></html>")
    } finally {
      attempt.driver.close()
    }
  }

}
