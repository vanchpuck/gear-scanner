package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

object PlanetaSportParser extends Parser[Product]{

  val StoreName = "www.planeta-sport.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = Option(doc.select("h1.productHeader").first.ownText)
      val brand = Option(doc.select("h1.productHeader b").text)
      val category = doc.select("li.breadcrumbs__item a").asScala.drop(1).dropRight(1).map(e => e.text())//.drop(2).map(e => e.text())
      val oldPrice: Option[Float] = doc.select("span.oldPrise").text() match {
        case "" => None
        case somePrice => Some(Util.parsePrice(somePrice))
      }
      val price = Option(Util.parsePrice(doc.select("span[itemprop=price]").text()))
      val baseUrl = new URL(new URL(url), "/")
      val imageUrl = new URL(baseUrl, doc.select("div#bigImg img").attr("src")).toString
      Product(url, StoreName, brand, title, category, price, oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }
}
