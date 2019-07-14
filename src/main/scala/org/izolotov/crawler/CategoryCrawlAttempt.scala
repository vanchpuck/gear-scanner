package org.izolotov.crawler

import org.izolotov.crawler.parser.product.Product

case class CategoryCrawlAttempt(url: String,
                                httpCode: Option[Int],
                                responseTime: Option[Long],
                                fetchError: Option[String],
                                category: Option[Product]) {

}
