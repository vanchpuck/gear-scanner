//package org.izolotov.crawler.timeoutscraper
//
//import java.net.URL
//
//import com.google.common.base.Strings
//import org.izolotov.crawler.parser.origin.BlackDiamondParser.BrandName
//import org.izolotov.crawler.parser.origin.{OriginCategory, OriginProduct}
//import org.jsoup.nodes.Document
//
//class BDParser extends JsoupHttpParser[OriginCategory] {
//  override protected def parse(categoryUrl: URL, doc: Document): OriginCategory = {
//    import scala.collection.JavaConverters._
//    val baseURL = new URL(categoryUrl, "/")
//    OriginCategory(
//      Option(doc.select("li.pager-next a").attr("href"))
//        .filter(href => !Strings.isNullOrEmpty(href))
//        .map(href => new URL(baseURL, href)),
//      doc.select("div.prlist").asScala.map(el => {
//        OriginProduct(
//          BrandName,
//          el.select("h2.grey-stripe").text(),
//          new URL(baseURL, el.select("img").attr("src")).toString
//        )
//      })
//    )
//  }
//}
