package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.Currency
import org.json4s.jackson.JsonMethods
import org.jsoup.Jsoup

object ReiCoopParser extends Parser[Product] {

  val StoreName = "www.rei.com"

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    implicit val formats = org.json4s.DefaultFormats
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val data = doc.select("script[type=application/ld+json]").first.data()
      val dataMap = JsonMethods.parse(data).extract[Map[String, Any]]
      val title = Option(dataMap("name").toString)
      val brand = Option(dataMap("brand").asInstanceOf[Map[String, String]]("name"))
      val metaData = doc.select("script[data-client-store=page-meta-data]").first.data()
      val metaDataMap = JsonMethods.parse(metaData).extract[Map[String, Any]]
      val category = metaDataMap("productCategoryPath").toString.split('|').drop(1)
      val price = Option(metaDataMap("displayPrice").toString.toFloat)
      val oldPrice = None
      val baseUrl = new URL(url, "/")
      val imageData = doc.select("script[data-client-store=image-data]").first.data()
      val imageDataMap = JsonMethods.parse(imageData).extract[Map[String, Any]]
      val imgUri: String = imageDataMap("media").asInstanceOf[List[Map[String, String]]](0)("uri")
      val imageUrl = new URL(baseUrl, imgUri).toString
      Product(urlString, host, brand, title, category, price, oldPrice, Some(Currency.USD.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = urlString, store = host, parseError = Some(e.toString))
    }
  }

}
