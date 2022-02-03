package org.izolotov.crawler.v4

import java.net.URL

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.CloseableHttpClient



//object HttpFetcher {
//  case class Response(response: CloseableHttpResponse, redirectUrl: Option[String]) extends Redirectable[CloseableHttpResponse]
//}

class HttpFetcherOld()(implicit httpClient: CloseableHttpClient) extends Fetcher[Redirectable[CloseableHttpResponse]] {

  // TODO handle redirects
  def fetch(url: URL): Redirectable[CloseableHttpResponse] = {
    val httpGet = new HttpGet(url.toString)
    val response: CloseableHttpResponse = httpClient.execute(httpGet)
    Redirectable(None, response)
  }

}
