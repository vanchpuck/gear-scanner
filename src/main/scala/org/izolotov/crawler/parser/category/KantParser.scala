package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object KantParser extends JsoupParser[Category] {

  val StoreName = "www.kant.ru"

  override protected def parse(categoryUrl: URL, doc: Document): Category = {
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      Option(doc.select("div.kant__pagination a:contains(Дальше)").first()).map(element => new URL(categoryUrl, element.attr("href"))),
      doc.select("div.kant__catalog__item__content a").asScala.map(f => f.attr("href")).map(url => new URL(baseURL, url))
    )
  }
}
