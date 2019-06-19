package org.izolotov.crawler

import java.net.URL

import org.apache.http.entity.ContentType
import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.parser.category.Category

import scala.compat.java8.OptionConverters._
import scala.util.Try

class ProductCategory(pageURL: String, parser: Parser[Category], fetcher: DelayFetcher, fetchTimeout: Long = Long.MaxValue) extends Iterable[Iterable[Try[URL]]] {

  class CategoryIterator(pageURL: String, fetcher: DelayFetcher, fetchTimeout: Long) extends Iterator[Iterable[Try[URL]]] {
    var nextURL: Option[String] = Option(pageURL)
    var nextCategory: Category = null//parsePage(currentURL)

    // TODO handle the fetch errors
    def parsePage(pageURL: String): Category = {
      val resp = fetcher.fetch(pageURL.toString, fetchTimeout)
      //      val exc = resp.getException.get()
      val response = resp.getResponse.asScala.get
      val entity = response.getEntity
      val content = entity.getContent
      val charset = ContentType.getOrDefault(response.getEntity).getCharset
      val category: Category = parser.parse(pageURL, entity.getContent, charset)
      content.close()
      response.close()
      category
    }

    override def hasNext: Boolean = {
      nextURL.isDefined
    }

    override def next(): Iterable[Try[URL]] = {
      nextCategory = parsePage(nextURL.get)
      nextURL = nextCategory.nextURL.map(f => f.toString)//getOr.toString //new URL(baseURL, currentDoc.select("li.bx-pag-next a").first().attr("href")).toString
      nextCategory.productURLs
    }
  }

  val baseURL: URL = new URL(new URL(pageURL), "/")
  val categoryIterator = new CategoryIterator(pageURL, fetcher, fetchTimeout)

  override def iterator: Iterator[Iterable[Try[URL]]] = {
    categoryIterator
  }

}
