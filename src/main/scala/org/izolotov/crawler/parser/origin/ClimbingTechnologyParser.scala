package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object ClimbingTechnologyParser extends JsoupParser[OriginCategory] {

  val BrandName = "climbing technology"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      None,
      doc.select("div.focus-single").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("h2.single-title").text(),
            new URL(baseURL, wrapper.select("img").attr("src")).toString,
          )
        })
    )
  }

}
