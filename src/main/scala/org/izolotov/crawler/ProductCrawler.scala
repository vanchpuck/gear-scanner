package org.izolotov.crawler

import java.net.URL

import org.apache.commons.httpclient.HttpStatus
import org.apache.http.client.config.RequestConfig
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.spark.sql.{Dataset, SparkSession}
import org.izolotov.crawler.parser.ProductParserRepo

case class ProductCrawler(userAgent: String,
                          delay: Long = 0L,
                          connectionTiemout: Int = Int.MaxValue,
                          connectionRequestTimeout: Int = Int.MaxValue,
                          socketTiemout: Int = Int.MaxValue)(implicit spark: SparkSession) {

  def crawl(urls: Dataset[UncrawledURL]): Dataset[ProductCrawlAttempt] = {
    // TODO handle errors during URL object construction
    // TODO avoid grouping
    import spark.implicits._
    urls.groupByKey(url => new URL(url.url).getHost).flatMapGroups((host, urls) => {
      val httpClient = HttpClients.custom()
        .setUserAgent(userAgent)
        .setConnectionManager(new PoolingHttpClientConnectionManager)
        .setDefaultRequestConfig(RequestConfig.custom().
          setRedirectsEnabled(false).
          setConnectionRequestTimeout(connectionRequestTimeout).
          setConnectTimeout(connectionTiemout).
          setSocketTimeout(socketTiemout).
          build())
        .build()
      val fetcher = new DelayFetcher(httpClient, delay)
      urls.map(url => url.url).map(url => {
        val attempt = fetcher.fetch(url)
        if (attempt.getResponse.isPresent) {
          val response = attempt.getResponse.get
          val content = response.getEntity.getContent
          val responseCode = response.getStatusLine.getStatusCode
          val doc: Option[Product] = if (responseCode == HttpStatus.SC_OK) {
            Some(ProductParserRepo.parse(host, url, content, ContentType.getOrDefault(response.getEntity).getCharset))
          } else {
            None
          }
          content.close()
          response.close()
          new ProductCrawlAttempt(url, Some(responseCode), Some(attempt.getResponseTime.get), None, doc)
        } else {
          new ProductCrawlAttempt(
            url,
            None,
            Some(attempt.getResponseTime.get),
            if (attempt.getException.isPresent) Some(attempt.getException.get.toString) else None,
            None
          )
        }
      })
    })
  }

}
