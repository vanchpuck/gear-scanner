package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.{Currency, Parser, Product, Util}
import org.jsoup.Jsoup

import scala.util.{Failure, Success, Try}

object TramontanaParser extends Parser[Product] {

  val StoreName = "tramontana.ru"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    import scala.collection.JavaConverters._
    Try(Jsoup.parse(inStream, charset.name(), url)) match {
      case Success(doc) => {
        try {
          val brand = doc.select("#product-all-brands > span").first.text()
          val title = doc.select("h1.text-uppercase").first.text()
          val category = doc.select(".breadcrumb-item").asScala.map(e => e.text())
          val oldPrice: Option[Int] = doc.select("#product-old-price").text() match {
            case "" => None
            case price => Some(Util.parsePrice(price))
          }
          val currPrice: Int = Util.parsePrice(doc.select("#product-price").first.text())
          new Product(
            url,
            StoreName,
            brand,
            title,
            category,
            oldPrice.getOrElse(currPrice), //currPrice.getOrElse(-1) else oldPrice.getOrElse(-1),
            if (oldPrice.isDefined) Some(currPrice) else None,
            Currency.Rub.toString
          )
          //        match {
          //          case "" => Try(-1)
          //          case price => Util.parsePrice(price)
          //        }

//          oldPrice match {
//            case Failure(throwable) => new Product(url = url, parseError = throwable.toString)
//            case Success(oldPriceVal) => {
//              currPrice match {
//                case Failure(throwable) => new Product(url = url, parseError = throwable.toString)
//                case Success(currPriceVal) => {
//                  new Product(
//                    url,
//                    brand,
//                    title,
//                    category,
//                    if (oldPriceVal != -1) oldPriceVal else currPriceVal, //currPrice.getOrElse(-1) else oldPrice.getOrElse(-1),
//                    if (oldPriceVal != -1) Some(currPrice.get) else None,
//                    Currency.Rub.toString
//                  )
//                }
//              }
//            }
//          }
        } catch {
          case exc: Exception => new Product(url = url, store = StoreName, parseError = Some(exc.toString))
        }
      }
      case Failure(ex) => null //new Product(url, None, None, None, None, None, None, Some(ex.toString))
    }
  }
}
