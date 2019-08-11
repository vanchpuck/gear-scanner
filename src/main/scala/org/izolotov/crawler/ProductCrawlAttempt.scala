package org.izolotov.crawler

import java.sql.Timestamp

import org.izolotov.crawler.parser.product.Product

case class ProductCrawlAttempt(url: String,
                               timestamp: Timestamp,
                               httpCode: Option[Int],
                               responseTime: Option[Long],
                               fetchError: Option[String],
                               document: Option[Product])
