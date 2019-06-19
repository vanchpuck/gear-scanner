package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Product, Util}
import org.jsoup.Jsoup

object PlanetaSportParser extends Parser[Product]{

  val StoreName = "planeta-sport.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = doc.select("h1.productHeader").first.ownText
      val brand = doc.select("h1.productHeader b").text
      val category = doc.select("li.breadcrumbs__item a").asScala.drop(1).dropRight(1).map(e => e.text())//.drop(2).map(e => e.text())
      val oldPrice: Option[Float] = doc.select("span.oldPrise").text() match {
        case "" => None
        case somePrice => Some(Util.parsePrice(somePrice))
      }
      val price = Util.parsePrice(doc.select("span[itemprop=price]").text())
      Product(url, StoreName, brand, title, category, price, oldPrice, Currency.Rub.toString)
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }
}
