package org.izolotov.crawler

import java.net.URL

import org.apache.http.entity.ContentType
import org.izolotov.crawler.parser.category.CategoryParserRepo
import org.izolotov.crawler.parser.category.Category

import scala.compat.java8.OptionConverters._
import scala.util.Try

class CategoryCrawlQueue(pageURL: URL,
                         fetcher: DelayFetcher,
                         fetchDelay: Long,
                         fetchTimeout: Long = Long.MaxValue,
                         hostConf: Map[String, CrawlConfiguration] = Map.empty) extends Iterable[Option[String]] {

  class CategoryIterator(pageURL: URL, fetcher: DelayFetcher, fetchDelay: Long, fetchTimeout: Long) extends Iterator[Iterable[Option[String]]] {
    val host = pageURL.getHost
    var nextURL: Option[URL] = Option(pageURL)
    var nextCategory: Category = null//parsePage(currentURL)
//    CategoryParserRepo.parse(null, null, null, null)
    // TODO handle the fetch errors
    def parsePage(pageURL: URL): Category = {
      val resp = fetcher.fetch(pageURL.toString, fetchDelay, fetchTimeout, Util.createHttpContext(host, hostConf.get(host)).orNull)
      //      val exc = resp.getException.get()
      val response = resp.getResponse.asScala.get
      val entity = response.getEntity
      val content = entity.getContent
      val charset = ContentType.getOrDefault(response.getEntity).getCharset
      val category: Category = CategoryParserRepo.parse(host, pageURL, entity.getContent, charset)
      content.close()
      response.close()
      category
    }

    override def hasNext: Boolean = {
      nextURL.isDefined
    }

    override def next(): Iterable[Option[String]] = {
      nextCategory = parsePage(nextURL.get)
      nextURL = nextCategory.nextURL//.map(f => f.toString)
      nextCategory.productURLs
    }
  }

  val baseURL: URL = new URL(pageURL, "/")
  val categoryIterator = new CategoryIterator(pageURL, fetcher, fetchDelay, fetchTimeout)

  override def iterator: Iterator[Option[String]] = {
    categoryIterator.flatten
  }

}
