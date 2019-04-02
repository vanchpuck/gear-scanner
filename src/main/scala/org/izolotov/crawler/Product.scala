package org.izolotov.crawler

case class Product(url: String,
                   store: String,
                   brand: String = null,
                   name: String = null,
                   category: Seq[String] = null,
                   price: Int = -1,
                   salePrice: Option[Int] = None,
                   currency: String = null,
                   parseError: Option[String] = None)
