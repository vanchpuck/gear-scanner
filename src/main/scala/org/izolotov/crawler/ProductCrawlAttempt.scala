package org.izolotov.crawler

case class ProductCrawlAttempt(url: String,
                               httpCode: Option[Int],
                               responseTime: Option[Long],
                               fetchError: Option[String],
                               document: Option[Product])
