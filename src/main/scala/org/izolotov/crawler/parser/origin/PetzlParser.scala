package org.izolotov.crawler.parser.origin

import java.net.URL

import com.google.common.base.Strings
import org.izolotov.crawler.parser.category.JsoupParser
import org.jsoup.nodes.Document

object PetzlParser extends JsoupParser[OriginCategory] {

  val BrandName = "petzl"
  val ImgUrlPattern = "background-image: url\\(([^\\)]*)\\);".r

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    OriginCategory(
      Option(doc.select("div.pagination-pages a.next").attr("href"))
        .filter(href => !Strings.isNullOrEmpty(href))
        .map(href => new URL(baseURL, href)),
      doc.select("div.prod").asScala.map(el => {
        val ImgUrlPattern(imageUrl) = el.select("a._img").attr("style")
        OriginProduct(
          BrandName,
          el.select("a._model").text(),
          new URL(baseURL, imageUrl).toString
        )
      })
    )
  }
}
