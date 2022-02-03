package org.izolotov.crawler.timeoutscraper

import java.net.{MalformedURLException, UnknownHostException}
import java.time.{Clock, Instant, ZoneId}
import java.util.concurrent.TimeoutException

import org.izolotov.crawler.timeoutscraper.FixedDelayModeratorSpec.Port
import org.scalatest.{BeforeAndAfter}
import HttpScraperSpec._
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.eclipse.jetty.server.Server
import org.izolotov.crawler.DelayFetcherTest
import org.scalatest.flatspec.AnyFlatSpec

object HttpScraperSpec {
  val ConnectionRefusedUrl = s"http://localhost:${Port + 1}/refused.html"
  val UnknownHostUrl = s"http://___unknown_host_test___/index.html"
  val MalformedUrl = s"^^:*#!?,.localhost:${Port}/malformed.html"
}

class HttpScraperSpec extends AnyFlatSpec with BeforeAndAfter {

  var server = new Server(Port)
  implicit val httpClient: CloseableHttpClient = HttpClients.createDefault;
  implicit val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC"))

  before {
    server.setHandler(new DelayFetcherTest.RequestHandler)
    server.start()
  }

  after {
    server.stop()
  }

  it should "handle the connection refused state" in {
    intercept[HttpHostConnectException] {
      new HttpFetcher().fetch(ConnectionRefusedUrl)
    }
  }

  it should "handle the unknown host scraping" in {
    intercept[UnknownHostException] {
      new HttpFetcher().fetch(UnknownHostUrl)
    }
  }

  it should "handle the malformed url scraping" in {
    intercept[MalformedURLException] {
      new HttpFetcher().fetch(MalformedUrl)
    }
  }

}
