package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.Currency
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.jsoup.Jsoup

object ReiGarageParser extends Parser[Product] {

  val StoreName = "www.rei.com"

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    implicit val formats = org.json4s.DefaultFormats
    val urlString = url.toString
    val host = url.getHost
    try {
      val doc = Jsoup.parse(inStream, charset.name(), urlString)
      val data = doc.select("script#page-data").first.data()
      val map = JsonMethods.parse(data).extract[Map[String, Any]]
      val product = map("product").asInstanceOf[Map[String, Any]]
      val title = Option(product("cleanTitle").toString)
      val brand = Option(product("brand").asInstanceOf[Map[String, String]]("label"))
      val category = product("categories").asInstanceOf[List[Map[String, Any]]].head("path").asInstanceOf[List[Map[String, String]]]
        .map(cat => cat("label")).drop(1)
      val price = Option(product("displayPrice").asInstanceOf[Map[String, Double]]("min").toFloat)
      val oldPrice = Option(product("displayPrice").asInstanceOf[Map[String, Double]]("compareAt").toFloat)
      val baseUrl = new URL(url, "/")
      val imageUrl = new URL(baseUrl, product("media").asInstanceOf[List[Map[String, String]]](0)("link")).toString
      Product(urlString, host, brand, title, category, price, oldPrice, Some(Currency.USD.toString), Some(imageUrl))
    } catch {
      case e: Exception => new Product(url = urlString, store = host, parseError = Some(e.toString))
    }
  }
}
