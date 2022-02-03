package org.izolotov.crawler.v2

import java.net.URL
import java.time.Clock

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.HttpClients

import scala.util.Try

class DefaultScraper[Doc, Attempt >: ScrapingAttempt[Doc]]()(implicit clock: Clock) extends Scraper[Doc, Attempt] {

  def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = {
    val startTime = clock.instant.toEpochMilli
    val raw = Try(fetcher.fetch(target))
    val responseTime = clock.instant.toEpochMilli - startTime
    val response = raw.map(content => parser.parse(target.url, content))
    ScrapingAttempt(target.url, startTime, raw.toOption.map(_ => responseTime), response.get)
  }
}
