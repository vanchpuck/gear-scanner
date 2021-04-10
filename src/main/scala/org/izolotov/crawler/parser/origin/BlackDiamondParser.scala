package org.izolotov.crawler.parser.origin

import java.net.URL

import com.google.common.base.Strings
import org.izolotov.crawler.parser.JsoupParser
import org.izolotov.crawler.parser.origin.PetzlParser.BrandName
import org.jsoup.nodes.Document

object BlackDiamondParser extends JsoupParser[OriginCategory] {

  val BrandName = "black diamond"

  override def parse(categoryUrl: URL, doc: Document): OriginCategory = {
    import scala.collection.JavaConverters._
    val baseURL = new URL(categoryUrl, "/")
    OriginCategory(
      Option(doc.select("li.pager-next a").attr("href"))
        .filter(href => !Strings.isNullOrEmpty(href))
        .map(href => new URL(baseURL, href)),
      doc.select("div.prlist").asScala.map(el => {
        OriginProduct(
          BrandName,
          el.select("h2.grey-stripe").text(),
          new URL(baseURL, el.select("img").attr("src")).toString
        )
      })
    )
  }

}
