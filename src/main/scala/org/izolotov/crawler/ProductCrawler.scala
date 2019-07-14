package org.izolotov.crawler

import java.net.URL

import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.spark.sql.{Dataset, SparkSession}

case class ProductCrawler(partitionsNum: Int,
                          userAgent: String,
                          fetchTimeout: Long,
                          fetchDelay: Long = 0L,
                          connectionTimeout: Int = Int.MaxValue,
                          connectionRequestTimeout: Int = Int.MaxValue,
                          socketTimeout: Int = Int.MaxValue)(implicit spark: SparkSession) {

  def crawl(urls: Dataset[UncrawledURL]): Dataset[ProductCrawlAttempt] = {
    // TODO handle errors during URL object construction
    import spark.implicits._
    urls.map(url => HostURL(url.url, new URL(url.url).getHost))
      .repartition(partitionsNum, $"host")
      .mapPartitions{
        iterator =>
          val httpClient = HttpClients.custom()
            .setUserAgent(userAgent)
            .setConnectionManager(new PoolingHttpClientConnectionManager)
            .setDefaultRequestConfig(RequestConfig.custom().
              setRedirectsEnabled(false).
              setConnectionRequestTimeout(connectionRequestTimeout).
              setConnectTimeout(connectionTimeout).
              setSocketTimeout(socketTimeout).
              build())
            .build()
          // TODO the 'iterator-to-iterator' paradigm violation. Consider using iterator without 'toList' converting
          new CrawlQueue(iterator.toList, httpClient, fetchDelay, fetchTimeout)
      }
  }

}
