package org.izolotov.crawler.parser.category
import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object DenSurkaParser extends JsoupParser {
  val StoreName = "www.densurka.ru"
  override protected def parse(categoryUrl: URL, doc: Document): Category = {
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      Option(doc.select("li.pager__item--next a").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("div.field__item a").asScala.map(f => f.attr("href")).map(url => new URL(baseURL, url))
    )
  }
}
