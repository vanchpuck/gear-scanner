package org.izolotov.crawler

import java.net.URL
import java.time.Clock

import org.apache.http.client.config.{CookieSpecs, RequestConfig}
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}

object ProductCrawler {

  def crawl[A](
                userAgent: String,
                urls: Dataset[UncrawledURL],
                partitionsNum: Int,
                fetchTimeout: Long,
                defaultParser: Class[_],
                typeTag: scala.reflect.runtime.universe.TypeTag[CrawlAttempt[A]],
                fetchDelay: Long = 0L,
                threadNum: Int = 1,
                connectionTimeout: Int = Int.MaxValue,
                connectionRequestTimeout: Int = Int.MaxValue,
                socketTimeout: Int = Int.MaxValue,
                hostConf: Map[String, CrawlConfiguration] = Map.empty
              )(implicit spark: SparkSession, clock: Clock): Dataset[CrawlAttempt[A]] = {
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
          new CrawlQueue[A](iterator.toList, httpClient, delay, timeout, threads, hostConf = hostCrawlConf, defaultParserClass = defaultParser)(clockInstance)
      }(Encoders.product[CrawlAttempt[A]](typeTag))
  }

}
