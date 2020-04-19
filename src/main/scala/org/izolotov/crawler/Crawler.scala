package org.izolotov.crawler

import java.net.URL
import java.sql.Timestamp
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

import org.apache.http.HttpStatus

import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.izolotov.crawler.parser.Parser

import scala.collection.JavaConverters._
import scala.compat.java8.OptionConverters._
import scala.concurrent.{ExecutionContext, Future}

object Crawler {

  private val fetcherMap: scala.collection.concurrent.Map[String, DelayFetcher] = new ConcurrentHashMap[String, DelayFetcher]().asScala

  def crawl[A](url: String)(implicit clock: Clock, ec: ExecutionContext, conf: CrawlConf, parser: Parser[A]): Future[CrawlAttempt[A]] = {
    val host = new URL(url).getHost
    // TODO move under Try
    val fetcher = fetcherMap.getOrElseUpdate(
      host,
      new DelayFetcher(
        HttpClients.custom()
          // TODO user agent
          .setUserAgent(conf.userAgent)
          .setConnectionManager(new PoolingHttpClientConnectionManager)
          .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD)
            .setRedirectsEnabled(false)
            .build())
          .build()
      )
    )
    Future {
      // TODO maybe use Promise instead of future
      // TODO add httpContext
      val fetched: FetchAttempt[CloseableHttpResponse] =
        fetcher.fetch(url, conf.delay, conf.timeout, conf.cookies.map(cookies => Util.httpContext(host, cookies)).orNull)
      parse(fetched, parser)
    }
  }

  private def parse[A](fetched: FetchAttempt[CloseableHttpResponse], parser: Parser[A])(implicit clock: Clock): CrawlAttempt[A] = {
    val timestamp = Timestamp.from(clock.instant()).getTime
    fetched.getResponse.asScala.map{
      response =>
        val content = response.getEntity.getContent
        try {
          val responseCode = response.getStatusLine.getStatusCode
          // TODO move this login on another layer
          val doc: Option[A] = if (responseCode == HttpStatus.SC_OK) {
            Some(parser.parse(new URL(fetched.getUrl), content, ContentType.getOrDefault(response.getEntity).getCharset))
          } else {
            None
          }
          new CrawlAttempt[A](fetched.getUrl, timestamp, Some(responseCode), Option(fetched.getResponseTime.get), None, doc)
        } finally {
          content.close()
          response.close()
        }
    }.getOrElse(
      CrawlAttempt[A](fetched.getUrl, timestamp, None, fetched.getResponseTime.asScala.map(_.toLong), fetched.getException.asScala.map(_.toString), None)
    )
  }

}
