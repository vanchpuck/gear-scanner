package org.izolotov.crawler.v2

import scala.util.Try

case class ScrapingAttempt[Doc](url: String, timestamp: Long, responseTime: Option[Long], response: Doc) extends ResponseTrait[Doc]
