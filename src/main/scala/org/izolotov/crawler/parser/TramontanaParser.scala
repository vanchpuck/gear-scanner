package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.{Currency, Parser, Product, Util}
import org.jsoup.Jsoup

object TramontanaParser extends Parser[Product] {

  val StoreName = "tramontana.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val brand = doc.select("#product-all-brands > span").first.text()
      val title = doc.select("h1.text-uppercase").first.text()
      val category = doc.select(".breadcrumb-item").asScala.map(e => e.text())
      val oldPrice: Option[Int] = doc.select("#product-old-price").text() match {
        case "" => None
        case price => Some(Util.parsePrice(price))
      }
      val price: Int = Util.parsePrice(doc.select("#product-price").first.text())
      Product(url, StoreName, brand, title, category, price, oldPrice, Currency.Rub.toString)
    } catch {
      case exc => Product(url = url, store = StoreName, parseError = Some(exc.toString))
    }
  }
}
