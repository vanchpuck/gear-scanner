package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try


object SportMarafonParser extends JsoupParser {

//  val StoreName = "sport-marafon.ru"

  override def parse(categoryUrl: URL, doc: Document): Category = {
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      Option(doc.select("a.navigate__link_next").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("div#main-catalog a.product-list__item-link").asScala.map(f => f.attr("href")).map(url => new URL(baseURL, url))
    )
  }
}
