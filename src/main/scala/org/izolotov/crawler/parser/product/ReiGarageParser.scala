package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.izolotov.crawler.Currency
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.jsoup.Jsoup

object ReiGarageParser extends Parser[Product] {

  val StoreName = "rei.com"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    implicit val formats = org.json4s.DefaultFormats
    try {
      val doc = Jsoup.parse(inStream, charset.name(), url)
      val data = doc.select("script#page-data").first.data()
      val map = JsonMethods.parse(data).extract[Map[String, Any]]
      val product = map("product").asInstanceOf[Map[String, Any]]
      val title = product("cleanTitle").toString
      val brand = product("brand").asInstanceOf[Map[String, String]]("label")
      val category = product("categories").asInstanceOf[List[Map[String, Any]]].head("path").asInstanceOf[List[Map[String, String]]]
        .map(cat => cat("label")).drop(1)
      val price = product("displayPrice").asInstanceOf[Map[String, Double]]("min").toFloat
      val oldPrice = Option(product("displayPrice").asInstanceOf[Map[String, Double]]("compareAt").toFloat)
      Product(url, StoreName, brand, title, category, price, oldPrice, Currency.USD.toString)
    } catch {
      case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
    }
  }
}
