package org.izolotov.crawler.v2

import java.net.URL
import java.time.Clock

import org.apache.http.HttpEntity
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.cookie.ClientCookie
import org.apache.http.impl.client.{BasicCookieStore, CloseableHttpClient}
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.protocol.{BasicHttpContext, HttpContext}
import org.izolotov.crawler.timeoutscraper.ScrapingAttempt

class HttpFetcher()(implicit httpClient: CloseableHttpClient, clock: Clock) extends Fetcher[HttpWebPage, CloseableHttpResponse] {

  def fetch(target: HttpWebPage): CloseableHttpResponse = {
    val httpGet = new HttpGet(target.url);
    val response: CloseableHttpResponse = httpClient.execute(httpGet)
    response
  }

}
