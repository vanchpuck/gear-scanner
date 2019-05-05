package org.izolotov.crawler

case class Product(url: String,
                   store: String,
                   brand: String = null,
                   name: String = null,
                   category: Seq[String] = null,
                   price: Float = -1,
                   oldPrice: Option[Float] = None,
                   currency: String = null,
                   parseError: Option[String] = None)
