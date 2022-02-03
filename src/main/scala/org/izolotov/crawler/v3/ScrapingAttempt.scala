package org.izolotov.crawler.v3

case class ScrapingAttempt[Doc](url: String, timestamp: Long, responseTime: Option[Long], response: Doc)
