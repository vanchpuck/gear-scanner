package org.izolotov.crawler.timeoutscraper

import java.net.URL
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

import org.apache.http.client.methods.CloseableHttpResponse

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class HttpScraper()(implicit clock: Clock) extends Scraper[CloseableHttpResponse, HttpFetchAttempt] {

//  private val queueMap: scala.collection.concurrent.Map[String, FetchingQueue] = new ConcurrentHashMap[String, FetchingQueue]().asScala

//  def fetch[T](url: String, fetcher: (String) => T, queueFactory: () => FetchingQueue)(implicit executionContext: ExecutionContext): Future[T] = {
//    val host = new URL(url).getHost
//    val queue = queueMap.getOrElseUpdate(
//      host,
//      queueFactory.apply()
//    )
//    queue.add(url, fetcher)
//  }
//  override def extract(url: String)(implicit fetcher: BaseFetcher[Response]): Future[Response] = {
//    null
//  }
//  override def extract(url: String)(implicit fetcher: BaseFetcher[Response]): Future[Response] = {
//    val host = new URL(url).getHost
//    val queue = queueMap.getOrElseUpdate(
//      host,
//      queueFactory.apply()
//    )
//    queue.add(url, fetcher.fetch)
//  }
//  def extrac1t(url: String)(implicit fetcher: BaseFetcher[Response]): Future[Response] = {
//    val host = new URL(url).getHost
//    val queue = queueMap.getOrElseUpdate(
//      host,
//      queueFactory.apply()
//    )
//    queue.add(url, fetcher.fetch)
//  }
  override def extract(url: String, fetcher: BaseFetcher[CloseableHttpResponse]): HttpFetchAttempt = {
    val startTime = clock.instant.toEpochMilli
    val response = fetcher.fetch(url)
    val elapsedTime = clock.instant.toEpochMilli - startTime
    HttpFetchAttempt(url, startTime, elapsedTime, response.getStatusLine.getStatusCode, response.getEntity)
  }
}
