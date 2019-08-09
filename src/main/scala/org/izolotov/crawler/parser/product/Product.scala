package org.izolotov.crawler.parser.product

case class Product(url: String,
                   store: String,
                   brand: Option[String] = None,
                   name: Option[String] = None,
                   category: Seq[String] = Seq.empty,
                   price: Option[Float] = None,
                   oldPrice: Option[Float] = None,
                   currency: Option[String] = None,
                   parseError: Option[String] = None)
