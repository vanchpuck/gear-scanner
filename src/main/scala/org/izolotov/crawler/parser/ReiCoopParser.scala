package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.{Currency, Parser, Product}
import org.json4s.jackson.JsonMethods
import org.jsoup.Jsoup

object ReiCoopParser extends Parser[Product] {

  val StoreName = "rei.com"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    implicit val formats = org.json4s.DefaultFormats
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val data = doc.select("script[type=application/ld+json]").first.data()
      val dataMap = JsonMethods.parse(data).extract[Map[String, Any]]
      val title = dataMap("name").toString
      val brand = dataMap("brand").asInstanceOf[Map[String, String]]("name")
      val metaData = doc.select("script[data-client-store=page-meta-data]").first.data()
      val metaDataMap = JsonMethods.parse(metaData).extract[Map[String, Any]]
      val category = metaDataMap("productCategoryPath").toString.split('|').drop(1)
      val price = metaDataMap("displayPrice").toString.toFloat
      val oldPrice = None
      Product(url, StoreName, brand, title, category, price, oldPrice, Currency.USD.toString)
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }

}
