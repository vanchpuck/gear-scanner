package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.category.JsoupParser
import org.jsoup.nodes.Document

object KongParser extends JsoupParser[OriginCategory] {

  val BrandName = "kong"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("div.col").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("span.title").text(),
            new URL(baseURL, wrapper.select("div.detail img").attr("src")).toString,
          )
        })
    )
  }

}
