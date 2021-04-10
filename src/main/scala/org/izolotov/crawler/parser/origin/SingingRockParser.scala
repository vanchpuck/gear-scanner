package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object SingingRockParser extends JsoupParser[OriginCategory] {

  val BrandName = "singing rock"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("div.list-goods-item").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("div.content h3").text(),
            new URL(baseURL, wrapper.select("img.lazy").attr("data-src")).toString,
          )
        })
    )
  }
}

