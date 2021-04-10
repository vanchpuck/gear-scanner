package org.izolotov.crawler.parser.origin

import java.net.URL

import com.google.common.base.Strings
import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object LaSportivaParser extends JsoupParser[OriginCategory] {

  val BrandName = "la sportiva"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    new OriginCategory(
      Option(doc.select("a.next").attr("href"))
        .filter(href => !Strings.isNullOrEmpty(href))
        .map(href => new URL(baseURL, href)),
      doc.select("div.product-item-info").asScala
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

