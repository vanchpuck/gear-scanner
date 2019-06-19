package org.izolotov.crawler

case class CategoryCrawlAttempt(url: String,
                                httpCode: Option[Int],
                                responseTime: Option[Long],
                                fetchError: Option[String],
                                category: Option[Product]) {

}
