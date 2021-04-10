package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object EdelridParser extends JsoupParser[OriginCategory] {

  val BrandName = "edelrid"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("div.overview-grid-item").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("div.item-title").text(),
            new URL(baseURL, wrapper.select("img").attr("src")).toString,
          )
        })
    )
  }
}
