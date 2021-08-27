package org.izolotov.crawler.v2

import java.net.URL

import org.apache.http.client.methods.CloseableHttpResponse

class HttpParser extends Parsable[CloseableHttpResponse, String] {
  override def parse(url: String, t: CloseableHttpResponse): String = {
    t.getStatusLine.getStatusCode.toString
  }
}
