package org.izolotov.crawler.parser.category

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object BackcountryParser extends JsoupParser[Category] {

  val StoreName = "www.backcountry.com"

  override def parse(categoryUrl: URL, doc: Document): Category = {
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      Option(doc.select("li.pag-next a").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("a.ui-pl-link").asScala
        .map(f => f.attr("href").replaceAll("\\?.*$", ""))
        .map(url => new URL(baseURL, url))
    )
  }
}
