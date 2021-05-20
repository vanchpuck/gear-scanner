package org.izolotov.crawler.parser.origin

import java.net.URL

import org.izolotov.crawler.parser.JsoupParser
import org.jsoup.nodes.Document

object PetzlParser extends JsoupParser[OriginCategory] {

  val BrandName = "petzl"
  val ImgUrlPattern = "background-image : url\\('([^\\)]*)'\\);".r

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    OriginCategory(
      None,
      doc.select("div.product").asScala.map(el => {
        val ImgUrlPattern(imageUrl) = el.select("span.productImage").attr("style")
        OriginProduct(
          BrandName,
          el.select("span.productTitle").text().replaceAll("®", ""),
          new URL(baseURL, imageUrl).toString
        )
      })
    )
  }
}
