package org.izolotov.crawler

case class SiteMapCrawlAttempt(url: String,
                               httpCode: Option[Int],
                               responseTime: Option[Long],
                               fetchError: Option[String],
                               sitemaps: Option[Seq[SiteMap]]) {

}
