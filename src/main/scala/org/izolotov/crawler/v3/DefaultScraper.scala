package org.izolotov.crawler.v3

import java.time.Clock

import scala.util.Try

class DefaultScraper[Raw, Doc](fetcher: (String) => Raw, parser: (String, Raw) => Doc)(implicit clock: Clock) {

  def extract(url: String): ScrapingAttempt[Doc] = {
    val startTime = clock.instant.toEpochMilli
    val raw = Try(fetcher.apply(url))
    val responseTime = clock.instant.toEpochMilli - startTime
    val response = raw.map(content => parser.apply(url, content))
    ScrapingAttempt(url, startTime, raw.toOption.map(_ => responseTime), response.get)
  }
}
