package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object AlpindustriaParser extends JsoupParser {
  override def parse(categoryUrl: String, doc: Document): Category = {
    val baseURL = new URL(new URL(categoryUrl), "/")
    new Category(
      Option(doc.select("a.next").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("a._model").asScala.map(f => f.attr("href")).map(url => Try(new URL(baseURL, url)))
    )
  }
}
