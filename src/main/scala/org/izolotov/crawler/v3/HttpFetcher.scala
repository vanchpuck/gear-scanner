package org.izolotov.crawler.v3

import java.time.Clock

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.CloseableHttpClient

class HttpFetcher()(implicit httpClient: CloseableHttpClient, clock: Clock) {

  def fetch(target: String): CloseableHttpResponse = {
    val httpGet = new HttpGet(target);
    val response: CloseableHttpResponse = httpClient.execute(httpGet)
    response
  }

}
