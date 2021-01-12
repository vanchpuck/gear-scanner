package org.izolotov.crawler.parser.origin

import java.net.URL

object OriginProduct {
  val Kind = "original"
}

case class OriginProduct(brand: String, name: String, imageUrl: URL)
