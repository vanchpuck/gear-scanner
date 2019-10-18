package org.izolotov.crawler

import java.net.URL
import java.time.Clock

import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.protocol.HttpContext
import org.apache.spark.sql.{Dataset, SparkSession}

class ProductCrawler(partitionsNum: Int,
                     userAgent: String,
                     fetchTimeout: Long,
                     fetchDelay: Long = 0L,
                     threadNum: Int = 1,
                     connectionTimeout: Int = Int.MaxValue,
                     connectionRequestTimeout: Int = Int.MaxValue,
                     socketTimeout: Int = Int.MaxValue,
                     hostConf: Map[String, CrawlConfiguration] = Map.empty)(implicit spark: SparkSession, clock: Clock) {

  def crawl(urls: Dataset[UncrawledURL]): Dataset[ProductCrawlAttempt] = {
    // TODO handle errors during URL object construction
    import spark.implicits._
    val partitions = partitionsNum
    val agent = userAgent
    val timeout = fetchTimeout
    val delay = fetchDelay
    val threads = threadNum
    val connTimeout = connectionTimeout
    val requestTimeout = connectionRequestTimeout
    val sockTimeout = socketTimeout
    val clockInstance = clock
    val hostCrawlConf = hostConf
    urls.map(url => HostURL(url.url, new URL(url.url).getHost))
      .repartition(partitions, $"host")
      .mapPartitions{
        iterator =>
          val httpClient = HttpClients.custom()
            .setUserAgent(agent)
            .setConnectionManager(new PoolingHttpClientConnectionManager)
            .setDefaultRequestConfig(RequestConfig.custom()
              .setCookieSpec(CookieSpecs.STANDARD)
              .setRedirectsEnabled(false)
              .setConnectionRequestTimeout(requestTimeout)
              .setConnectTimeout(connTimeout)
              .setSocketTimeout(sockTimeout)
              .build())
            .build()
          new CrawlQueue(iterator.toList, httpClient, delay, timeout, threads, hostConf = hostCrawlConf)(clockInstance)
      }
  }

}
