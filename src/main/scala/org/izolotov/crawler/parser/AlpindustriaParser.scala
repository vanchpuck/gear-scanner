package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.{Currency, Parser, Product, Util}
import org.jsoup.Jsoup

object AlpindustriaParser extends Parser[Product] {

  val StoreName = "alpindustria.ru"

  /**
    * This implementation sets the NullPointerException error message if some mandatory of fields is absent
    */
  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = doc.select(".product_main-title").first.text
      val brand = doc.select(".product_pat-label img[title]").first.attr("title")
      val category = doc.select("div.breadcrumbs a").asScala.drop(1).map(e => e.text())
      val salePrice: Option[Int] = doc.select("span.product_pri").text match {
        case "" => None
        case somePrice => Some(Util.parsePrice(somePrice))
      }
      val (price: Int, oldPrice: Option[Int]) = salePrice match {
        case Some(sale) => (sale, Some(Util.parsePrice(doc.select("span.product_striked").text())))
        case None => (Util.parsePrice(doc.select("span.product_pri_all").text()), None)
      }
      Product(url, StoreName, brand, title, category, price, oldPrice, Currency.Rub.toString)
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }
}