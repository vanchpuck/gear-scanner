package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

object EquipParser extends Parser[Product] {

  val StoreName = "equip.ru"

  //TODO C.A.M.P should be cleared of dots
  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = Option(doc.select("div.product_top_right h1").first.text)
      val brand = Option(doc.select("div.brand span").text)
      val category = Seq.empty
      val oldPrice: Option[Float] = doc.select("div.with_old_price span.old_product_price_in").first() match {
        case null => None
        case somePrice => Some(Util.parsePrice(somePrice.ownText()))
      }
      val price = Option(Util.parsePrice(doc.select("div.product_price > span").first().ownText()))
      Product(url, StoreName, brand, title, category, price, oldPrice, Some(Currency.Rub.toString))
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }

}
