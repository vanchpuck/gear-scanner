package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

class TramontanaParser extends Parser[Product] {

  val StoreName = "tramontana.ru"

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val brand = Option(doc.select("#product-all-brands > span").first.text())
      val title = Option(doc.select("h1.text-uppercase").first.text())
      val category = doc.select(".breadcrumb-item").asScala.map(e => e.text())
      val oldPrice: Option[Float] = doc.select("#product-old-price").text() match {
        case "" => None
        case price => Some(Util.parsePrice(price))
      }
      val price: Option[Float] = Option(Util.parsePrice(doc.select("#product-price").first.text()))
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(baseUrl, doc.select("div.product-images-list img").first().attr("src")).toString
      Product(urlString, host, brand, title, category, price, oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case exc: Exception => Product(url = urlString, store = host, parseError = Some(exc.toString))
    }
  }
}
