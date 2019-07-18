package org.izolotov.crawler

import org.izolotov.crawler.parser.product.Product

case class ProductCrawlAttempt(url: String,
                               httpCode: Option[Int],
                               responseTime: Option[Long],
                               fetchError: Option[String],
                               document: Option[Product])
