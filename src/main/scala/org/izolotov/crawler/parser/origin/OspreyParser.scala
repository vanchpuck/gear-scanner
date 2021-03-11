package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.category.JsoupParser
import org.jsoup.nodes.Document

object OspreyParser extends JsoupParser[OriginCategory] {

  val BrandName = "osprey"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("li.product-item").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("a.product-item-link").text(),
            new URL(baseURL, wrapper.select("img.product-image-photo").attr("src")).toString,
          )
        })
    )
  }
}
