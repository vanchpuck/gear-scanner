package org.izolotov.crawler.parser.origin

object OriginProduct {
  val Kind = "original"
}

case class OriginProduct(brand: String, name: String, imageUrl: String)
