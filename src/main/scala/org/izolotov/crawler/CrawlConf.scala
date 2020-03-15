package org.izolotov.crawler

case class CrawlConf(userAgent: String,
                     delay: Long,
                     timeout: Long,
                     cookies: Option[Map[String, _]] = None)
