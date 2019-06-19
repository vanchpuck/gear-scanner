package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Product, Util}
import org.jsoup.Jsoup

object KantParser extends Parser[Product] {

  val StoreName = "kant.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = doc.select("div.kant__product__fixed__title").first().ownText()
      val brand = doc.select("div.kant__product__detail-item > span:containsOwn(Бренд) + span").text()
      val category = doc.select(".list_links > li > a").asScala.drop(1).map(_.ownText())
      val price: Float = Util.parsePrice(doc.select("span.kant__product__price__new").first().text)
      val oldPrice: Option[Float] = doc.select("span.kant__product__price__old").first match {
        case null => None
        case price => Some(Util.parsePrice(price.text))
      }
      Product(url, StoreName, brand, title, category, price, oldPrice, Currency.Rub.toString)
    } catch {
      case exc: Exception => Product(url, StoreName, parseError = Some(exc.toString))
    }
  }

}
