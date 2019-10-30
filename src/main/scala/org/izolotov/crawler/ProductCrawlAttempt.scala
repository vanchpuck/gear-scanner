package org.izolotov.crawler

import java.sql.Timestamp

import com.google.common.base.Strings
import org.izolotov.crawler.parser.product.Product

case class ProductCrawlAttempt(url: String,
                               timestamp: Timestamp,
                               httpCode: Option[Int],
                               responseTime: Option[Long],
                               fetchError: Option[String],
                               document: Option[Product]) {
  require(!Strings.isNullOrEmpty(url), "The URL field can't be an empty string or null")
  require(timestamp != null, "The Timestamp field can't be null")
  require(fetchError.getOrElse(null) != "", "The Fetch error field can't be null or an empty string")
}
