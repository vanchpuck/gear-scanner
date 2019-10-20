package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.{Currency, Util}
import org.izolotov.crawler.parser.Parser
import org.jsoup.Jsoup

object DenSurkaParser extends Parser[Product] {

  val StoreName = "www.densurka.ru"

  /**
    * This implementation sets the NullPointerException error message if some mandatory of fields is absent
    */
  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = Option(doc.select("h1").first.text)
      val pattern = "^(.*)\\s*–.*$".r
      val pattern(brand) = doc.select("div.field__item a img").attr("alt")
      val category = doc.select("span.breadcrumb span a").asScala.drop(1).map(e => e.attr("title"))
      val price = Util.parsePrice(doc.select("div.sprice span.amount").first().text())
      val oldPrice: Option[Float] = Option(doc.select("div.lprice span.amount").first()).map(p => Util.parsePrice(p.text()))
      val imageUrl = doc.select("div.products-images img").first().attr("src")
      Product(url, StoreName, Option(brand.trim), title, category, Option(price), oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }

}
