package org.izolotov.crawler

import java.net.URL

import org.apache.http.entity.ContentType
import org.izolotov.crawler.parser.category.CategoryParserRepo
import org.izolotov.crawler.parser.category.Category

import scala.compat.java8.OptionConverters._
import scala.util.Try

class CategoryCrawlQueue(pageURL: String, fetcher: DelayFetcher, fetchTimeout: Long = Long.MaxValue) extends Iterable[Option[String]] {

  class CategoryIterator(pageURL: String, fetcher: DelayFetcher, fetchTimeout: Long) extends Iterator[Iterable[Option[String]]] {
    val host = new URL(pageURL).getHost
    var nextURL: Option[String] = Option(pageURL)
    var nextCategory: Category = null//parsePage(currentURL)
//    CategoryParserRepo.parse(null, null, null, null)
    // TODO handle the fetch errors
    def parsePage(pageURL: String): Category = {
      val resp = fetcher.fetch(pageURL.toString, fetchTimeout)
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
      nextURL = nextCategory.nextURL.map(f => f.toString)//getOr.toString //new URL(baseURL, currentDoc.select("li.bx-pag-next a").first().attr("href")).toString
      nextCategory.productURLs//.map(url => url)
    }
  }

  val baseURL: URL = new URL(new URL(pageURL), "/")
  val categoryIterator = new CategoryIterator(pageURL, fetcher, fetchTimeout)

  override def iterator: Iterator[Option[String]] = {
    categoryIterator.flatten
  }

}
