package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.category.JsoupParser
import org.jsoup.nodes.Document

object GrivelParser extends JsoupParser[OriginCategory] {

  val BrandName = "grivel"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      Option(doc.select(".Pagination__NavItem[rel=next]").first()).map(element => new URL(baseURL, element.attr("href"))),
      doc.select(".ProductItem__Wrapper").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select(".ProductItem__Title").text(),
            new URL(baseURL, wrapper.select(".ProductItem__Wrapper noscript img:not(.ProductItem__Image--alternate)").attr("src")).toString,
          )
        })
    )
  }
}
