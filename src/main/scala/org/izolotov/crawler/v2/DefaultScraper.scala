package org.izolotov.crawler.v2

import java.net.URL
import java.time.Clock

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.HttpClients

import scala.util.Try

class DefaultScraper[Doc, Attempt >: ScrapingAttempt[Doc]]()(implicit clock: Clock) extends Scraper[Doc, Attempt] {

//  override def extract[T <: Target, Raw, Attempt <: ResponseTrait[Doc]](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt =
  def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = {
    val startTime = clock.instant.toEpochMilli
    val raw = Try(fetcher.fetch(target))
    val responseTime = clock.instant.toEpochMilli - startTime
    val response = raw.map(content => parser.parse(target.url, content))
    ScrapingAttempt(target.url, startTime, raw.toOption.map(_ => responseTime), response.get)
  }

//  override def extract[T <: Target, Raw, Doc](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): ScrapingAttempt[String] = ???
//  override def extract[T <: Target, Raw, Doc <: String](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): ScrapingAttempt[String] = ???
//  override def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, String]): ScrapingAttempt[String] = ???
//  override def extract[T <: Target, Raw, Doc, R](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): R = {
//    val startTime = clock.instant.toEpochMilli
//    val raw = Try(fetcher.fetch(target))
//    val responseTime = clock.instant.toEpochMilli - startTime
//    val response = raw.map(content => parser.parse(target.url, content))
//    ScrapingAttempt(target.url, startTime, raw.toOption.map(_ => responseTime), response)
//  }
//  override def extract[T <: Target, Raw, Attempt: {def response() : Doc}](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = ???
//  override def extract[T <: Target, Raw, Attempt >: {def response() : Doc}](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = {
//    val startTime = clock.instant.toEpochMilli
//    val raw = Try(fetcher.fetch(target))
//    val responseTime = clock.instant.toEpochMilli - startTime
//    val response = raw.map(content => parser.parse(target.url, content)).get
//    val a: Attempt = ScrapingAttempt(target.url, startTime, raw.toOption.map(_ => responseTime), response)
//    a
////    ScrapingAttempt(target.url, startTime, raw.toOption.map(_ => responseTime), response)
//  }
//  override def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): ResponseTrait[Doc] = ???
//  override def extract[T <: Target, Raw, Attempt <: ResponseTrait[Doc]](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = ???
//  override def extract[T <: Target, Raw, Attempt <: ResponseTrait[Doc]](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt = ???
}
