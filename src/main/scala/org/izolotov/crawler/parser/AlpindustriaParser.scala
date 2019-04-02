package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.{Currency, Parser, Product, Util}
import org.jsoup.Jsoup

import scala.util.{Failure, Success, Try}

object AlpindustriaParser extends Parser[Product] {

  val StoreName = "alpindustria.ru"

  /**
    * This implementation sets the NullPointerException error message if some mandatary field is absent
    */
  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    Try(Jsoup.parse(inStream, charset.name(), url)) match {
      case Success(doc) =>
        try {
          val title = doc.select(".product_main-title").first.text
          val brand = doc.select(".product_pat-label img[title]").first.attr("title")
          val category = doc.select("div.breadcrumbs a").asScala.drop(1).map(e => e.text())
          val salePrice: Try[Option[Int]] = doc.select("span.product_pri").text match {
            case "" => Try(None)
            case price => Try(Some(Util.parsePrice(price)))
          }
          salePrice match {
            case Failure(throwable) => Product(url = url, store = StoreName, parseError = Some(throwable.toString))
            case Success(salePriceVal: Option[Int]) => {
              val originPrice: Try[Int] = salePriceVal match {
                case None => Try(Util.parsePrice(doc.select("span.product_pri_all").first.text()))
                case Some(_) => Try(Util.parsePrice(doc.select("span.product_striked").first.text()))
              }
              originPrice match {
                case Failure(throwable) => Product(url = url, store = StoreName, parseError = Some(throwable.toString))
                // TODO try to use Currency instance as input parameter
                case Success(price) => Product(url, StoreName, brand, title, category, price, salePriceVal, Currency.Rub.toString)
              }
            }
          }
        } catch {
          case e: Exception => new Product(url = url, store = StoreName, parseError = Some(e.toString))
        }
      case Failure(throwable) => new Product(url = url, store = StoreName, parseError = Some(throwable.toString))
    }
  }
}