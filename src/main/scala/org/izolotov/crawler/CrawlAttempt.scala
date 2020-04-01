package org.izolotov.crawler

case class CrawlAttempt[A](url: String,
                           timestamp: Long,
                           httpCode: Option[Int],
                           responseTime: Option[Long],
                           fetchError: Option[String],
                           document: Option[A])
