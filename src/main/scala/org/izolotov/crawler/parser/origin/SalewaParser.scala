package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object SalewaParser extends JsoupParser[OriginCategory] {

  val BrandName = "salewa"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      Option(doc.select("a.page--next:not(.is--hidden)").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select("div.product--info").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("h2.product--title").text(),
            new URL(baseURL, wrapper.select("span.image--media").select("img").attr("data-src")).toString,
          )
        })
    )
  }

}
