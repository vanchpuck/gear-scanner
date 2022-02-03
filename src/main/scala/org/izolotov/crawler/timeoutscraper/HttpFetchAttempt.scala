package org.izolotov.crawler.timeoutscraper

import java.io.InputStream

import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse

case class HttpFetchAttempt(url: String, timestamp: Long, responseTime: Long, httpCode: Int, response: HttpEntity)
