package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object OcunParser extends JsoupParser[OriginCategory] {

  val BrandName = "ocun"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("div.card-product").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("h3.h5").text(),
            new URL(baseURL, wrapper.select("img.img-fluid").attr("src")).toString,
          )
        })
    )
  }
}
