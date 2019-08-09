package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.{Currency, Util}
import org.jsoup.Jsoup

object SportMarafonParser extends Parser[Product] {

  val StoreName = "sport-marafon.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val title = Option(doc.select("h1.catalog-detail__name").first().text())
      val brand = Option(doc.select("a.catalog-detail__brand > img").first().attr("title"))
      val category = doc.select("li.breadcrumbs__item a").asScala.drop(2).map(e => e.text())
      val price = Util.parsePrice(doc.select("div.catalog-detail__price").select(":not(div.catalog-detail__price_old)").first().text())
      val oldPrice = Option(doc.select("div.catalog-detail__price").select("div.catalog-detail__price_old").first())
        .map(p => Util.parsePrice(p.text()))
      Product(url, StoreName, brand, title, category, Option(price), oldPrice, Some(Currency.Rub.toString))
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }
}
