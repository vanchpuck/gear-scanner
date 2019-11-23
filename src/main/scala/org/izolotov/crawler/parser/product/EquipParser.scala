package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

class EquipParser extends Parser[Product] {

  //TODO C.A.M.P should be cleared of dots
  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val title = Option(doc.select("div.product_top_right h1").first.text)
      val brand = Option(doc.select("div.brand span").text)
      val category = Seq.empty
      val oldPrice: Option[Float] = doc.select("div.with_old_price span.old_product_price_in").first() match {
        case null => None
        case somePrice => Some(Util.parsePrice(somePrice.ownText()))
      }
      val price = Option(Util.parsePrice(doc.select("div.product_price > span").first().ownText()))
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(baseUrl, doc.select("img#img_cont").first().attr("src")).toString
      Product(urlString, host, brand, title, category, price, oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = urlString, store = host, parseError = Some(e.toString))
    }
  }

}
