package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

class SportMarafonParser extends Parser[Product] {

  val StoreName = "sport-marafon.ru"

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val title = Option(doc.select("h1.catalog-detail__name").first().text())
      val brand = Option(doc.select("a.catalog-detail__brand > img").first().attr("title"))
      val category = doc.select("li.breadcrumbs__item a").asScala.drop(2).map(e => e.text()).toSeq
      val price = Util.parsePrice(doc.select("div.catalog-detail__price").select(":not(div.catalog-detail__price_old)").first().text())
      val oldPrice = Option(doc.select("div.catalog-detail__price").select("div.catalog-detail__price_old").first())
        .map(p => Util.parsePrice(p.text()))
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(baseUrl, doc.select("ul.catalog-detail__slideshow li a").first().attr("href")).toString
      Product(urlString, host, brand, title, category, Option(price), oldPrice, Some(Currency.Rub.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = urlString, store = host, parseError = Some(e.toString))
    }
  }
}
