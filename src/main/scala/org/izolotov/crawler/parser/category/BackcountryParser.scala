package org.izolotov.crawler.parser.category

import java.net.URL

import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object BackcountryParser extends JsoupParser {

  val StoreName = "www.backcountry.com"

  override def parse(categoryUrl: String, doc: Document): Category = {
    val baseURL = new URL(new URL(categoryUrl), "/")
    new Category(
      Option(doc.select("li.pag-next a").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("a.ui-pl-link").asScala.map(f => f.attr("href")).map(url => Try(new URL(baseURL, url).toString).toOption)
    )
  }
}
