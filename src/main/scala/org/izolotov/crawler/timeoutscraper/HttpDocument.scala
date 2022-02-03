package org.izolotov.crawler.timeoutscraper

case class HttpDocument[A](url: String, timestamp: Long, fetchTime: Long, httpCode: Int, content: Option[A])
