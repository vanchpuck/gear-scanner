package org.izolotov.crawler.parser.category

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object TrekkinnParser extends JsoupParser[Category] {
  val StoreName = "www.trekkinn.com"

  override def parse(categoryUrl: URL, doc: Document): Category = {
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      None,
      doc.select("p.BoxPriceName a").asScala.map(f => f.attr("href")).map(url => new URL(baseURL, url))
    )
  }
}
