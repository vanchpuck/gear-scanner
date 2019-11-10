package org.izolotov.crawler

import java.net.URL

import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.util.Try

class CategoryCrawler(partitionsNum: Int,
                      userAgent: String,
                      fetchDelay: Long,
                      fetchTimeout: Long,
                      connectionRequestTimeout: Int = Int.MaxValue,
                      connectionTimeout: Int = Int.MaxValue,
                      socketTimeout: Int = Int.MaxValue,
                      hostConf: Map[String, CrawlConfiguration] = Map.empty) {

  def crawl(urls: Dataset[UncrawledURL]): Dataset[Option[String]] = {
    import urls.sparkSession.implicits._

    val userAgent = this.userAgent
    val fetchTimeout = this.fetchTimeout
    val fetchDelay = this.fetchDelay
    val connectionRequestTimeout = this.connectionRequestTimeout
    val connectionTimeout = this.connectionTimeout
    val socketTimeout = this.socketTimeout
    val hostConf = this.hostConf

    urls
      .map(url => HostURL(url.url, new URL(url.url).getHost))
      .repartition(partitionsNum, $"host")
      .mapPartitions{hostURLs =>
        val httpClient = HttpClients.custom()
          .setUserAgent(userAgent)
          .setConnectionManager(new PoolingHttpClientConnectionManager)
          .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD)
            .setRedirectsEnabled(false)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .setConnectTimeout(connectionTimeout)
            .setSocketTimeout(socketTimeout)
            .build())
          .build()
        val fetcher = new DelayFetcher(httpClient)
        // TODO host could be queried in parallel to increase the throughput
        hostURLs.map(uncrawled => new CategoryCrawlQueue(new URL(uncrawled.url), fetcher, fetchDelay, fetchTimeout, hostConf))
          .flatMap(urls => urls)
      }
  }

}
