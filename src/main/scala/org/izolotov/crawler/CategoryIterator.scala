package org.izolotov.crawler

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType
import org.izolotov.crawler.parser.category.CategoryParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CategoryIterator(categoryURL: String, fetcher: Fetcher[CloseableHttpResponse], parser: CategoryParser) extends Iterator[Iterable[String]]{

  import scala.compat.java8.OptionConverters._

  var nextURL: Option[String] = Some(categoryURL)

  def crawl(url: String): Document = {
    val attempt = fetcher.fetch(url)
    attempt.getResponse.asScala
      .map{
        response =>
          val content = response.getEntity.getContent
          val responseCode = response.getStatusLine.getStatusCode
          val charset = ContentType.getOrDefault(response.getEntity).getCharset
          val doc = Jsoup.parse(content, charset.name(), url)
          content.close()
          response.close()
          doc
      }
      .getOrElse(throw attempt.getException.get())
  }

  override def hasNext: Boolean = {
    nextURL.isDefined
  }

  override def next(): Iterable[String] = {
    nextURL.map{
      url =>
        val doc: Document = crawl(url)
        nextURL = parser.getNextURL(doc)
        parser.getProductURLs(doc)
    }.getOrElse(throw new NoSuchElementException("Category doesn't exist"))
  }
}
