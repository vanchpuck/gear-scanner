package org.izolotov.crawler.parser.category

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.util.Try

object ReiCoopParser extends JsoupParser[Category] {

  val StoreName = "www.rei.com"

  override def parse(categoryUrl: URL, doc: Document): Category = {
    //TODO Rei category sometimes contains the link to Rei garage
    val baseURL = new URL(categoryUrl, "/")
    new Category(
      Option(doc.select("a._154Z_uQClLJukPk_:contains(Go to next page)").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("a._2eRDCeQGq1DRmpd3").asScala.map(f => f.attr("href")).map(url => new URL(baseURL, url))
    )
  }
}
