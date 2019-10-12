package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object TrekkinnParser extends JsoupParser {
  val StoreName = "www.trekkinn.com"

  override def parse(categoryUrl: String, doc: Document): Category = {
    val baseURL = new URL(new URL(categoryUrl), "/")
    new Category(
      None,
      doc.select("p.BoxPriceName a").asScala.map(f => f.attr("href")).map(url => Try(new URL(baseURL, url).toString).toOption)
    )
  }
}
