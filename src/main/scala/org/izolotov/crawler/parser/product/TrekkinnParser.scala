package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

/**
  * Created by izolotov on 03.10.19.
  */
class TrekkinnParser extends Parser[Product] {

  val StoreName = "www.trekkinn.com"

  /**
    * This implementation sets the NullPointerException error message if some mandatory of fields is absent
    */
  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val title = Option(doc.select("h1.productName").first.text)
      val brand = Option(doc.select("div.logoMarca a").first.attr("title"))
      val category = doc.select("div.path_det a").asScala.drop(2).map(e => e.text()).toSeq
      val price = Util.parsePrice(doc.select("p#total_dinamic").first().text())
      val oldPrice = doc.select("span#descuento").first().text() match {
        case "" => None
        case _ => Some(Util.parsePrice(doc.select("span#precio_anterior").text))
      }
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(baseUrl, doc.select("p#imagen_princial img").first().attr("src")).toString
      Product(urlString, StoreName, brand, title, category, Option(price), oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = urlString, store = host, parseError = Some(e.toString))
    }
  }

}
