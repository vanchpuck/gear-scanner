package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

object KantParser extends Parser[Product] {

  val StoreName = "www.kant.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = Option(doc.select("div.kant__product__fixed__title").first().ownText())
      val brand = Option(doc.select("div.kant__product__detail-item > span:containsOwn(Бренд) + span").text())
      val category = doc.select(".list_links > li > a").asScala.drop(1).map(_.ownText())
      val price: Option[Float] = Option(Util.parsePrice(doc.select("span.kant__product__price__new").first().text))
      val oldPrice: Option[Float] = doc.select("span.kant__product__price__old").first match {
        case null => None
        case price => Some(Util.parsePrice(price.text))
      }
      val baseUrl = new URL(new URL(url), "/")
      val imageUrl = new URL(baseUrl, doc.select("div.kant__product__fixed__image img").attr("src")).toString
      Product(url, StoreName, brand, title, category, price, oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case exc: Exception => Product(url, StoreName, parseError = Some(exc.toString))
    }
  }

}
