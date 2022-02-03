package org.izolotov.crawler.v4

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.CloseableHttpClient

abstract class HttpScraper[A]()(implicit httpClient: CloseableHttpClient) extends Extractor[A] {

  def extract(url: String): A = {
    processResponse(fetch(url))
  }

  def fetch(url: String): CloseableHttpResponse = {
    val httpGet = new HttpGet(url.toString)
    val response: CloseableHttpResponse = httpClient.execute(httpGet)
    response
  }

  def processResponse(response: CloseableHttpResponse): A

}
