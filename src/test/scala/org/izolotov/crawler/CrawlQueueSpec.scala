package org.izolotov.crawler

import java.time.{Clock, Instant, ZoneId}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.apache.http.impl.client.HttpClients
import org.eclipse.jetty.server.{Request, Server}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.scalatest.{BeforeAndAfter, FlatSpec}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization
import CrawlQueueSpec._
import org.izolotov.crawler.parser.product.Product

object CrawlQueueSpec {
  val Port = 8082

  val Host1PetzlLynx = s"http://localhost:${Port}/petzl-lynx"
  val Host1BDCyborg = s"http://localhost:${Port}/dlack-diamond-cyborg"
  val Host1PetzlVasak = s"http://localhost:${Port}/petzl-vasak"
  val Host2GrivelRambo = s"http://127.0.0.1:${Port}/grivel-rambo"

  object RequestHandler extends AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
      implicit val formats = DefaultFormats
      val dummyProduct =
        Product(baseRequest.getRequestURL.toString, "store", Some("brand"), Some("name"), Seq("category"), Some(1F), None, Some(Currency.Rub.toString), None)
      response.setContentType("text/plain;charset=utf-8")
      response.setStatus(HttpServletResponse.SC_OK)
      response.getWriter.print(Serialization.write(dummyProduct))
      baseRequest.setHandled(true)
    }
  }
}

class CrawlQueueSpec extends FlatSpec with BeforeAndAfter {

  implicit val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

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
    val uncrawled = Seq(
      HostURL(s"http://localhost:${Port}/product", "localhost"),
      HostURL(s"http://127.0.0.1:${Port}/product", "127.0.0.1")
    )

    val startTime = System.currentTimeMillis()
    val crawled = new CrawlQueue(uncrawled, HttpClients.createDefault(), 300L, 1000L)
      .map(crawled => (crawled, System.currentTimeMillis() - startTime)).toList
    val elapsedTime = System.currentTimeMillis() - startTime

    assert(crawled.map(_._2).reduce((et1, et2) => et1 + et2) > elapsedTime)
  }

  it should "crawl all provided urls" in {
    val uncrawled = Seq(
      HostURL(s"http://localhost:${Port}/product", "localhost"),
      HostURL(s"http://localhost:${Port}/product", "localhost"),
      HostURL(s"http://localhost:${Port}/product", "localhost"),
      HostURL(s"http://localhost:${Port}/product", "localhost"),
      HostURL(s"http://127.0.0.1:${Port}/product", "127.0.0.1"),
      HostURL(s"http://127.0.0.1:${Port}/product", "127.0.0.1"),
      HostURL(s"http://127.0.0.1:${Port}/product", "127.0.0.1")
    )

    val actual = new CrawlQueue(uncrawled, HttpClients.createDefault(), 100L, 1000L)
    assert(actual.size == 7)
    actual.foreach(
      item =>
        assert(item.document.get == Product(item.url, "store", Some("brand"), Some("name"), Seq("category"), Some(1F), None, Some(Currency.Rub.toString), None))
    )
  }

  it should "not fail if no urls provided" in {
    val actual = new CrawlQueue(Seq(), HttpClients.createDefault(), 100L, 1000L).toList
    assert(actual.size == 0)
  }

}
