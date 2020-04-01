package org.izolotov.crawler.parser.product

import com.google.common.base.Strings

object Product {
  val Kind = "product"
}

case class Product(url: String,
                   store: String,
                   brand: Option[String] = None,
                   name: Option[String] = None,
                   category: Seq[String] = Seq.empty,
                   price: Option[Float] = None,
                   oldPrice: Option[Float] = None,
                   currency: Option[String] = None,
                   imageUrl: Option[String] = None,
                   parseError: Option[String] = None) {
  require(!Strings.isNullOrEmpty(url), "The URL field can't be an empty string or null")
  require(!Strings.isNullOrEmpty(store), "The Store field can't be an empty string or null")
  require(brand.getOrElse(null) != "", "The Brand field can't be an empty string")
  require(name.getOrElse(null) != "", "The Name field can't be null or an empty string")
  require(!category.contains(""), "Category list can't contain an empty string")
  require(currency.getOrElse(null) != "", "The Currency filed can't be null or an empty string")
  require(imageUrl.getOrElse(null) != "", "The Image URL field can't be null or an empty string")
  require(parseError.getOrElse(null) != "", "The Parse error field can't be null or an empty string")
}
