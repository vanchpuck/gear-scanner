package org.izolotov.crawler

import java.net.URL

import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.spark.sql.{Dataset, SparkSession}

case class SiteMapCrawler(userAgent: String,
                          delay: Long = 0L,
                          connectionTiemout: Int = Int.MaxValue,
                          connectionRequestTimeout: Int = Int.MaxValue,
                          socketTimeout: Int = Int.MaxValue)(implicit spark: SparkSession) {

  def crawl(urls: Dataset[UncrawledURL]): Dataset[SiteMapCrawlAttempt] = {
    import scala.compat.java8.OptionConverters._
    // TODO handle errors during URL object construction
    // TODO avoid grouping
    import spark.implicits._
    urls.groupByKey(sitemapURL => new URL(sitemapURL.url).getHost).flatMapGroups((host, urls) => {
      val httpClient = HttpClients.custom()
        .setUserAgent(userAgent)
        .setConnectionManager(new PoolingHttpClientConnectionManager)
        .setDefaultRequestConfig(RequestConfig.custom().
          setRedirectsEnabled(false).
          setConnectionRequestTimeout(connectionRequestTimeout).
          setConnectTimeout(connectionTiemout).
          setSocketTimeout(socketTimeout).
          build())
        .build()
      val fetcher = new DelayFetcher(httpClient)
      urls.map(siteMapURL => siteMapURL.url).map(url => {
        val attempt = fetcher.fetch(url, delay)
        attempt.getResponse.asScala match {
          case Some(response: CloseableHttpResponse) =>
            val content = response.getEntity.getContent
            try {
              val sitemaps = SitemapParser.parse(url, content)
              SiteMapCrawlAttempt(url, Some(response.getStatusLine.getStatusCode), Some(attempt.getResponseTime.get), None, Some(sitemaps))
            } finally {
              content.close()
              response.close()
            }
          case None =>
            SiteMapCrawlAttempt(
              url,
              None,
              attempt.getResponseTime.asScala.map(t => t.toLong),
              attempt.getException.asScala.filter(e => e != null).map(e => e.toString),
              None
            )
        }
      })
    })
  }
}
