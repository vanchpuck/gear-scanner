package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

class KantParser extends Parser[Product] {

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val title = Option(doc.select("div.kant__product__fixed__title").first().ownText())
      val brand = Option(doc.select("div.kant__product__detail-item > span:containsOwn(Бренд) + span").first().text())
      val category = doc.select(".list_links > li > a").asScala.drop(1).map(_.ownText()).toSeq
      val price: Option[Float] = Option(Util.parsePrice(doc.select("span.kant__product__price__new").first().text))
      val oldPrice: Option[Float] = doc.select("span.kant__product__price__old").first match {
        case null => None
        case price => Some(Util.parsePrice(price.text))
      }
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(baseUrl, doc.select("div.kant__product__fixed__image img").attr("src")).toString
      Product(urlString, host, brand, title, category, price, oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case exc: Exception => Product(urlString, host, parseError = Some(exc.toString))
    }
  }

}
