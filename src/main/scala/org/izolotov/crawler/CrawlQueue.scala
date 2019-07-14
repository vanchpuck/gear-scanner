package org.izolotov.crawler

import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import java.util.concurrent.atomic.AtomicInteger

import org.apache.commons.httpclient.HttpStatus
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.CloseableHttpClient
import org.izolotov.crawler.parser.product.{Product, ProductParserRepo}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class CrawlQueue(urls: Iterable[HostURL],
                 httpClient: CloseableHttpClient,
                 defaultFetchDelay: Long,
                 fetchTimeout: Long) extends Iterator[ProductCrawlAttempt]{

  import scala.compat.java8.OptionConverters._
  import ExecutionContext.Implicits.global

  var crawlCounter: AtomicInteger = null
  var resultQueue: LinkedBlockingQueue[ProductCrawlAttempt] = null
  this.synchronized {
    crawlCounter = new AtomicInteger(urls.size)
    resultQueue = new LinkedBlockingQueue[ProductCrawlAttempt]()
  }

  urls
    .groupBy(_.host)
    .mapValues(entry => entry.iterator)
    .iterator
    .foreach{ group =>
    val future = Future{
      // TODO close fetchers
      val fetcher = new DelayFetcher(httpClient, defaultFetchDelay)
      val host = group._1
      group._2.map{ unfetched =>
        val attempt = fetcher.fetch(unfetched.url, fetchTimeout)
        attempt.getResponse.asScala
          .map{
            response =>
              val content = response.getEntity.getContent
              val responseCode = response.getStatusLine.getStatusCode
              val doc: Option[Product] = if (responseCode == HttpStatus.SC_OK) {
                Some(ProductParserRepo.parse(host, unfetched.url, content, ContentType.getOrDefault(response.getEntity).getCharset))
              } else {
                None
              }
              content.close()
              response.close()
              new ProductCrawlAttempt(unfetched.url, Some(responseCode), Option(attempt.getResponseTime.get), None, doc)
          }
          .getOrElse(
            new ProductCrawlAttempt(
              unfetched.url,
              None,
              attempt.getResponseTime.asScala.map(rt => rt.toLong),
              attempt.getException.asScala.map(exc => exc.toString),
              None
            )
          )
      }
    }
    future onComplete {
      case Success(crawled) => crawled.foreach{data =>
        this.synchronized {
          resultQueue.add(data)
          crawlCounter.decrementAndGet()
        }
      }
      case Failure(exc) =>
        throw exc
    }
  }

  override def hasNext: Boolean = {
    this.synchronized {
      // TODO maybe we should close fether here !!!
      return !(crawlCounter.get() == 0 && resultQueue.peek() == null)
    }
  }

  override def next(): ProductCrawlAttempt = {
    val next = resultQueue.poll(fetchTimeout * 2, TimeUnit.MILLISECONDS)
    if (next != null) {
      next
    } else {
      throw new RuntimeException("Looks like CrawlQueue being stuck")
    }
  }
}
