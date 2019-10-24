package org.izolotov.crawler

import java.sql.Timestamp
import java.time.Clock
import java.util.concurrent.{Executors, LinkedBlockingQueue, TimeUnit}

import org.apache.commons.httpclient.HttpStatus
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.CloseableHttpClient
import org.izolotov.crawler.parser.product.{Product, ProductParserRepo}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class CrawlQueue(urls: Iterable[HostURL],
                 httpClient: CloseableHttpClient,
                 defaultFetchDelay: Long,
                 fetchTimeout: Long = Long.MaxValue,
                 threadNum: Int = 1,
                 hostConf: Map[String, CrawlConfiguration] = Map.empty)(implicit clock: Clock) extends Iterator[ProductCrawlAttempt]{

  import scala.compat.java8.OptionConverters._
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threadNum))


  var remain = urls.size
  val resultQueue = new LinkedBlockingQueue[ProductCrawlAttempt]()

  urls
    .groupBy(_.host)
    .mapValues(entry => entry.iterator)
    .iterator
    .foreach{ group =>
    val future: Future[Iterator[ProductCrawlAttempt]] = Future{
      val fetcher = new DelayFetcher(httpClient)
      val host = group._1
      group._2.map{ unfetched =>
        val timestamp = Timestamp.from(clock.instant())
        try {
          val crawlConf = hostConf.get(host)
          val attempt = fetcher.fetch (
            unfetched.url,
            crawlConf.map(c => c.getFetchDelay).getOrElse(defaultFetchDelay),
            fetchTimeout,
            Util.createHttpContext(host, crawlConf).orNull
          )
          attempt.getResponse.asScala
            .map {
              response =>
                val content = response.getEntity.getContent
                try {
                  val responseCode = response.getStatusLine.getStatusCode
                  val doc: Option[Product] = if (responseCode == HttpStatus.SC_OK) {
                    Some(ProductParserRepo.parse(host, unfetched.url, content, ContentType.getOrDefault(response.getEntity).getCharset))
                  } else {
                    None
                  }
                  new ProductCrawlAttempt(unfetched.url, timestamp, Some(responseCode), Option(attempt.getResponseTime.get), None, doc)
                } finally {
                  content.close()
                  response.close()
                }
            }
            .getOrElse(
              new ProductCrawlAttempt(
                unfetched.url,
                timestamp,
                None,
                attempt.getResponseTime.asScala.map(rt => rt.toLong),
                attempt.getException.asScala.map(exc => exc.toString),
                None
              )
            )
        } catch {
          case e: Throwable => new ProductCrawlAttempt(unfetched.url, timestamp, None, None, Some(e.toString), None)
        }
      }
    }
    future onComplete {
      case Success(crawled) => crawled.foreach{data =>
        resultQueue.put(data)
      }
      case Failure(exc) =>
        throw exc
    }
  }

  override def hasNext: Boolean = {
    remain > 0
  }

  override def next(): ProductCrawlAttempt = {
    val next = resultQueue.poll(fetchTimeout * 4, TimeUnit.MILLISECONDS)
    if (next != null) {
      remain-=1
      next
    } else {
      // Just in case. To avoid blocking.
      throw new RuntimeException(s"Looks like CrawlQueue being stuck. remain = ${remain}")
    }
  }
}
