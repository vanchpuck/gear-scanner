package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

object BackcountryParser extends Parser[Product] {

  val StoreName = "www.backcountry.com"

  /**
    * This implementation sets the NullPointerException error message if some mandatory of fields is absent
    */
  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = Option(doc.select("h1.product-name").first.ownText())
      val brand = Option(doc.select("span.qa-brand-name").first.text)
      val category = doc.select("a.qa-breadcrumb-link").asScala.map(e => e.text())
      val price = Option(Util.parsePrice(doc.select("span.product-pricing__retail, span.product-pricing__sale").first().text()))
      val oldPrice = Option(doc.select("span.product-pricing__inactive").first())
        .map(p => Util.parsePrice(p.text()))
      val baseUrl = new URL(new URL(url), "/")
      val imageUrl = new URL(baseUrl, doc.select("div.ui-flexzoom").first().attr("data-zoom")).toString
      Product(url, StoreName, brand, title, category, price, oldPrice, Some(Currency.USD.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }
}
