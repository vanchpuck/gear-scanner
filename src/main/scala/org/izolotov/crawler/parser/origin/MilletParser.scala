package org.izolotov.crawler.parser.origin

import java.net.URL

import com.google.common.base.Strings
import org.izolotov.crawler.parser.category.JsoupParser
import org.jsoup.nodes.Document

object MilletParser extends JsoupParser[OriginCategory] {

  val BrandName = "millet"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      Option(doc.select("a.next-page").attr("href"))
        .filter(href => !Strings.isNullOrEmpty(href))
        .map(href => new URL(baseURL, href)),
      doc.select("li.item").asScala
        .map(wrapper => {
          new OriginProduct(
            BrandName,
            wrapper.select("a.product-name-link").text(),
            Option(wrapper.select("a.product-image"))
              .map(a => a.select("img").attr("src"))
              .map(src => new URL(baseURL, src).toString())
              .orNull
          )
        })
    )
  }
}
