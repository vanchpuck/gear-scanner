package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object KantParser extends JsoupParser {
  override protected def parse(categoryUrl: String, doc: Document): Category = {
    val baseURL = new URL(new URL(categoryUrl), "/")
    new Category(
      Option(doc.select("div.kant__pagination a:contains(Дальше)").first()).map(element => new URL(new URL(categoryUrl), element.attr("href"))),
      doc.select("div.kant__catalog__item__content a").asScala.map(f => f.attr("href")).map(url => Try(new URL(baseURL, url)))
    )
  }
}
