package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object EquipParser extends JsoupParser {

  val StoreName = "equip.ru"

  override def parse(categoryUrl: URL, doc: Document): Category = {
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      Option(doc.select("div.pagelist a:contains(След.)").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("div.name a").asScala.map(f => f.attr("href")).map(url => new URL(baseURL, url))
    )
  }
}
