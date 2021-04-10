package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object CampParser extends JsoupParser[OriginCategory] {

  val BrandName = "camp"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("a.woocommerce-loop-product__link").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select(".woocommerce-loop-product__title").text(),
            new URL(baseURL, wrapper.select("img.attachment-woocommerce_thumbnail").attr("src")).toString,
          )
        })
    )
  }
}
