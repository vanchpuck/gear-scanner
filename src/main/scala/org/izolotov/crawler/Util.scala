package org.izolotov.crawler

import scala.util.Try

/**
  * Created by izolotov on 24.12.18.
  */
object Util {

//  def parsePrice(priceStr: String): Try[Int] = {
//    Try(priceStr.replaceAll("[^0-9]", "").toInt)
//  }

  def parsePrice(priceStr: String): Int = {
    priceStr.replaceAll("[^0-9]", "").toInt
  }

}
