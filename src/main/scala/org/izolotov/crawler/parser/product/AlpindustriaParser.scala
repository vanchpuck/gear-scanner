package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

class AlpindustriaParser extends Parser[Product] {

  val StoreName = "alpindustria.ru"

  /**
    * This implementation sets the NullPointerException error message if some mandatory of fields is absent
    */
  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val title = Option(doc.select(".product_main-title").first.text)
      val brand = Option(doc.select(".product_pat-label img[title]").first.attr("title"))
      val category = doc.select("div.breadcrumbs a").asScala.drop(1).map(e => e.text()).toSeq
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(
        baseUrl,
        doc.select("div.product_gallery-item img").first().attr("src")
      ).toString
      val salePrice: Option[Float] = doc.select("span.product_pri").text match {
        case "" => None
        case somePrice => Some(Util.parsePrice(somePrice))
      }
      val (price: Float, oldPrice: Option[Float]) = salePrice match {
        case Some(sale) => (sale, Some(Util.parsePrice(doc.select("span.product_striked").text())))
        case None => (Util.parsePrice(doc.select("span.product_pri_all").text()), None)
      }
      Product(urlString, host, brand, title, category, Option(price), oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = urlString, store = host, parseError = Some(e.toString))
    }
  }
}